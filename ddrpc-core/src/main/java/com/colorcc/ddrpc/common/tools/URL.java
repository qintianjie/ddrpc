package com.colorcc.ddrpc.common.tools;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * URL
 * 
 * 替换java.net.URL 线程安全
 * 
 */
public final class URL {

    private final String protocol;

    private final String username;

    private final String password;

    private final String host;

    private final int port;

    private final String path;

    private final Map<String, String> parameters;

    private final Map<String, Number> numbers = new ConcurrentHashMap<String, Number>();

    // cache
    private final transient String ip;

    private final transient String fullString;

    private final transient String identityString;

    private final transient String parameterString;

    private final transient String string;

    public static class Builder {
        private final String protocol;

        private final String host;

        private final int port;

        private String username;

        private String password;

        private String path;

        private Map<String, String> parameters = new HashMap<String, String>();

        public Builder(String protocol, String host, int port) {
            this.protocol = protocol;
            this.host = host;
            this.port = port;
        }

        public Builder(URL url) {
            this.protocol = url.getProtocol();
            this.host = url.getHost();
            this.port = url.getPort();
            this.username = url.getUsername();
            this.password = url.getPassword();
            this.path = url.getPath();
            this.parameters.putAll(url.getParameters());// 返回的是UnmodifiableMap
        }

        public Builder(String url) {
            this(valueOf(url));
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder params(String... pairs) {
            this.parameters.putAll(CollectionUtils.toStringMap(pairs));
            return this;
        }

        public Builder params(Map<String, String> parameters) {
            this.parameters.putAll(parameters);
            return this;
        }

        public Builder paramString(String query) {
            if (query == null || query.length() == 0) {
                return this;
            }
            return params(StringUtils.parseQueryString(query));
        }

        public Builder param(String key, String value) {
            if (key == null || key.length() == 0 || value == null || value.length() == 0) {
                return this;
            }
            this.parameters.put(key, value);
            return this;
        }

        public Builder paramAndEncoded(String key, String value) {
            if (value == null || value.length() == 0) {
                return this;
            }
            return param(key, encode(value));
        }

        public Builder param(String key, boolean value) {
            return param(key, String.valueOf(value));
        }

        public Builder param(String key, int value) {
            return param(key, String.valueOf(value));
        }

        public Builder param(String key, long value) {
            return param(key, String.valueOf(value));
        }

        public Builder param(String key, double value) {
            return param(key, String.valueOf(value));
        }

        public Builder param(String key, Enum<?> value) {
            if (value == null)
                return this;
            return param(key, String.valueOf(value));
        }

        public Builder param(String key, Number value) {
            if (value == null)
                return this;
            return param(key, String.valueOf(value));
        }

        public Builder paramIfAbsent(String key, String value) {
            if (key == null || key.length() == 0 || value == null || value.length() == 0) {
                return this;
            }
            String oldValue = parameters.get(key);
            if (oldValue == null || value.length() == 0) {
                oldValue = parameters.get(Constants.DEFAULT_KEY_PREFIX + key);
            }
            if (oldValue != null && oldValue.length() > 0) {
                return this;// has param
            }
            this.parameters.put(key, value);
            return this;
        }

        public URL build() {
            return new URL(this);
        }
    }

    public URL(Builder builder) {
        this(builder.protocol, builder.username, builder.password, builder.host, builder.port, builder.path,
                builder.parameters);
    }

    public URL(String protocol, String username, String password, String host, int port, String path,
            Map<String, String> parameters) {
        if ((username == null || username.length() == 0) && password != null && password.length() > 0) {
            throw new IllegalArgumentException("Invalid url, password without username!");
        }
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = (port < 0 ? 0 : port);

        // trim the beginning "/"
        while (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        this.path = path;
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        } else {
            parameters = new HashMap<String, String>(parameters);
        }
        this.parameters = Collections.unmodifiableMap(parameters);

        // build cache
        parameterString = buildParameters();
        identityString = buildString(true);
        fullString = identityString + (parameterString.length() > 0 ? '?' + parameterString : "");
        // no username and passwd
        string = buildString(false) + (parameterString.length() > 0 ? '?' + parameterString : "");
        ip = NetUtils.getIpByHost(host);
    }

    /**
     * Parse url string
     * 
     * @param url URL string
     * @return URL instance
     */
    public static URL valueOf(String url) {
        if (url == null || (url = url.trim()).length() == 0) {
            throw new IllegalArgumentException("url == null");
        }
        String protocol = null;
        String username = null;
        String password = null;
        String host = null;
        int port = 0;
        String path = null;
        Map<String, String> parameters = null;
        int i = url.indexOf("?"); // seperator between body and parameters
        if (i >= 0) {
            String[] parts = url.substring(i + 1).split("\\&");
            parameters = new HashMap<String, String>();
            for (String part : parts) {
                part = part.trim();
                if (part.length() > 0) {
                    int j = part.indexOf('=');
                    if (j >= 0) {
                        parameters.put(part.substring(0, j), part.substring(j + 1));
                    } else {
                        parameters.put(part, part);
                    }
                }
            }
            url = url.substring(0, i);
        }
        i = url.indexOf("://");
        if (i >= 0) {
            if (i == 0)
                throw new IllegalStateException("url missing protocol: \"" + url + "\"");
            protocol = url.substring(0, i);
            url = url.substring(i + 3);
        } else {
            // case: file:/path/to/file.txt
            i = url.indexOf(":/");
            if (i >= 0) {
                if (i == 0)
                    throw new IllegalStateException("url missing protocol: \"" + url + "\"");
                protocol = url.substring(0, i);
                url = url.substring(i + 1);
            }
        }

        i = url.indexOf("/");
        if (i >= 0) {
            path = url.substring(i + 1);
            url = url.substring(0, i);
        }
        i = url.indexOf("@");
        if (i >= 0) {
            username = url.substring(0, i);
            int j = username.indexOf(":");
            if (j >= 0) {
                password = username.substring(j + 1);
                username = username.substring(0, j);
            }
            url = url.substring(i + 1);
        }
        i = url.indexOf(":");
        if (i >= 0 && i < url.length() - 1) {
            port = Integer.parseInt(url.substring(i + 1));
            url = url.substring(0, i);
        }
        if (url.length() > 0)
            host = url;
        return new URL(protocol, username, password, host, port, path, parameters);
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return port <= 0 ? host : host + ":" + port;
    }

    public String getPath() {
        return path;
    }

    public String getAbsolutePath() {
        if (path != null && !path.startsWith("/")) {
            return "/" + path;
        }
        return path;
    }

    public URL resetPath(String path) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public URL resetHost(String host) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public URL resetPort(int port) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public URL resetProtocol(String protocol) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public URL resetUsername(String username) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public URL resetPassword(String password) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getParameterAndDecoded(String key) {
        return getParameterAndDecoded(key, null);
    }

    public String getParameterAndDecoded(String key, String defaultValue) {
        return decode(getParameter(key, defaultValue));
    }

    /*
     * 根据key获取参数值,如果不存在值,则去取default.key表示的值
     */
    public String getParameter(String key) {
        String value = parameters.get(key);
        if (value == null || value.length() == 0) {
            value = parameters.get(Constants.DEFAULT_KEY_PREFIX + key);
        }
        return value;
    }

    public String getParameter(String key, String defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

    public String[] getParameter(String key, String[] defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Constants.COMMA_SPLIT_PATTERN.split(value);
    }

    public double getParameter(String key, double defaultValue) {
        Number n = numbers.get(key);
        if (n != null) {
            return n.doubleValue();
        }
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        double d = Double.parseDouble(value);
        numbers.put(key, d);
        return d;
    }

    public long getParameter(String key, long defaultValue) {
        Number n = numbers.get(key);
        if (n != null) {
            return n.longValue();
        }
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        long l = Long.parseLong(value);
        numbers.put(key, l);
        return l;
    }

    public int getParameter(String key, int defaultValue) {
        Number n = numbers.get(key);
        if (n != null) {
            return n.intValue();
        }
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        int i = Integer.parseInt(value);
        numbers.put(key, i);
        return i;
    }

    public boolean getParameter(String key, boolean defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public double getPositiveParameter(String key, double defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        double value = getParameter(key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public long getPositiveParameter(String key, long defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        long value = getParameter(key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public int getPositiveParameter(String key, int defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        int value = getParameter(key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public boolean hasParameter(String key) {
        String value = getParameter(key);
        return value != null && value.length() > 0;
    }

    public String getMethodParameterAndDecoded(String method, String key) {
        return URL.decode(getMethodParameter(method, key));
    }

    public String getMethodParameterAndDecoded(String method, String key, String defaultValue) {
        return URL.decode(getMethodParameter(method, key, defaultValue));
    }

    public String getMethodParameter(String method, String key) {
        String value = parameters.get(method + "." + key);
        if (value == null || value.length() == 0) {
            return getParameter(key);
        }
        return value;
    }

    public String getMethodParameter(String method, String key, String defaultValue) {
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

    public double getMethodParameter(String method, String key, double defaultValue) {
        String methodKey = method + "." + key;
        Number n = numbers.get(methodKey);
        if (n != null) {
            return n.intValue();
        }
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        double d = Double.parseDouble(value);
        numbers.put(methodKey, d);
        return d;
    }

    public long getMethodParameter(String method, String key, long defaultValue) {
        String methodKey = method + "." + key;
        Number n = numbers.get(methodKey);
        if (n != null) {
            return n.intValue();
        }
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        long l = Long.parseLong(value);
        numbers.put(methodKey, l);
        return l;
    }

    public int getMethodParameter(String method, String key, int defaultValue) {
        String methodKey = method + "." + key;
        Number n = numbers.get(methodKey);
        if (n != null) {
            return n.intValue();
        }
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        int i = Integer.parseInt(value);
        numbers.put(methodKey, i);
        return i;
    }

    public boolean getMethodParameter(String method, String key, boolean defaultValue) {
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public boolean hasMethodParameter(String method, String key) {
        if (method == null) {
            String suffix = "." + key;
            for (String fullKey : parameters.keySet()) {
                if (fullKey.endsWith(suffix)) {
                    return true;
                }
            }
            return false;
        }
        if (key == null) {
            String prefix = method + ".";
            for (String fullKey : parameters.keySet()) {
                if (fullKey.startsWith(prefix)) {
                    return true;
                }
            }
            return false;
        }
        String value = getMethodParameter(method, key);
        return value != null && value.length() > 0;
    }

    /**
     * 拷贝构造,仅提供参数全集的方式构建,单个参数逐步构建的使用Builder进行
     * @param parameters
     * @return
     */
    public URL addParameters(Map<String, String> parameters) {
        if (parameters == null || parameters.size() == 0) {
            return this;
        }
        Map<String, String> map = new HashMap<String, String>(getParameters());
        map.putAll(parameters);
        return new URL(protocol, username, password, host, port, path, map);
    }

    /**
     * 拷贝构造,仅提供参数全集的方式构建,单个参数逐步构建的使用Builder进行
     * @param parameters
     * @return
     */
    public URL addParameters(String... pairs) {
        return addParameters(CollectionUtils.toStringMap(pairs));
    }

    /**
     * 拷贝构造,仅提供参数全集的方式构建,单个参数逐步构造的使用Builder进行
     * @param parameters
     * @return
     */
    public URL addParameterString(String query) {
        if (query == null || query.length() == 0) {
            return this;
        }
        return addParameters(StringUtils.parseQueryString(query));
    }

    /**
     * 拷贝构造,仅提供参数全集的方式构建,单个参数逐步构造的使用Builder进行 调换一下顺序,以老的参数覆盖新的参数
     * @param parameters
     * @return
     */
    public URL addParametersIfAbsent(Map<String, String> parameters) {
        if (parameters == null || parameters.size() == 0) {
            return this;
        }
        Map<String, String> map = new HashMap<String, String>(parameters);
        map.putAll(getParameters());
        return new URL(protocol, username, password, host, port, path, map);
    }

    public URL removeParameter(String key) {
        if (key == null || key.length() == 0) {
            return this;
        }
        return removeParameters(key);
    }

    public URL removeParameters(Collection<String> keys) {
        if (keys == null || keys.size() == 0) {
            return this;
        }
        return removeParameters(keys.toArray(new String[0]));
    }

    public URL removeParameters(String... keys) {
        if (keys == null || keys.length == 0) {
            return this;
        }
        Map<String, String> map = new HashMap<String, String>(getParameters());
        for (String key : keys) {
            map.remove(key);
        }
        if (map.size() == getParameters().size()) {
            return this;
        }
        return new URL(protocol, username, password, host, port, path, map);
    }

    public URL clearParameters() {
        return new URL(protocol, username, password, host, port, path, new HashMap<String, String>());
    }

    @Override
    public String toString() {
        return string;
    }

    public String toIdentityString() {
        return identityString;
    }

    public String toFullString() {
        return fullString;
    }

    public String toParameterString() {
        return parameterString;
    }

    private String buildParameters() {
        StringBuilder buf = new StringBuilder();
        if (getParameters() != null && getParameters().size() > 0) {
            boolean first = true;
            for (Map.Entry<String, String> entry : new TreeMap<String, String>(getParameters()).entrySet()) {
                if (entry.getKey() != null && entry.getKey().length() > 0) {
                    if (first) {
                        first = false;
                    } else {
                        buf.append("&");
                    }
                    buf.append(entry.getKey());
                    buf.append("=");
                    buf.append(entry.getValue() == null ? "" : entry.getValue().trim());
                }
            }
        }
        return buf.toString();
    }

    private String buildString(boolean u) {
        StringBuilder buf = new StringBuilder();
        if (protocol != null && protocol.length() > 0) {
            buf.append(protocol);
            buf.append("://");
        }
        if (u && username != null && username.length() > 0) {
            buf.append(username);
            if (password != null && password.length() > 0) {
                buf.append(":");
                buf.append(password);
            }
            buf.append("@");
        }
        if (host != null && host.length() > 0) {
            buf.append(host);
            if (port > 0) {
                buf.append(":");
                buf.append(port);
            }
        }
        if (path != null && path.length() > 0) {
            buf.append("/");
            buf.append(path);
        }
        return buf.toString();
    }

    public java.net.URL toJavaURL() {
        try {
            return new java.net.URL(toFullString());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    public String getServiceKey() {
        String inf = getServiceName();
        if (inf == null)
            return null;
        StringBuilder buf = new StringBuilder();
        String group = getParameter(Constants.GROUP_KEY);
        if (group != null && group.length() > 0) {
            buf.append(group).append(":");
        }
        buf.append(inf);
        String version = getParameter(Constants.VERSION_KEY);
        if (version != null && version.length() > 0) {
            buf.append(":").append(version);
        }
        return buf.toString();
    }

    public String getServiceName() {
        return getParameter(Constants.INTERFACE_KEY, path);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + port;
        result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        URL other = (URL) obj;
        if (host == null) {
            if (other.host != null)
                return false;
        } else if (!host.equals(other.host))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (port != other.port)
            return false;
        if (protocol == null) {
            if (other.protocol != null)
                return false;
        } else if (!protocol.equals(other.protocol))
            return false;
        return true;
    }

    public static String encode(String value) {
        if (value == null || value.length() == 0) {
            return "";
        }
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String decode(String value) {
        if (value == null || value.length() == 0) {
            return "";
        }
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
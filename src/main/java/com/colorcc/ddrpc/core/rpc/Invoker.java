package com.colorcc.ddrpc.core.rpc;

import com.colorcc.ddrpc.core.define.DdrpcException;

public interface Invoker<T> extends Node {

	Class<T> getInterface();

	Result invoke(Invocation invocation) throws DdrpcException;

}

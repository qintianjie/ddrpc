package com.colorcc.ddrpc.transport.netty.callback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class ClientCallbackImpl<T> implements ClientCallback<T>,  Future<T>{
	private final CountDownLatch latch = new CountDownLatch(1);
	private T data;
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

//	@Override
//	public ClientCallback<T> callback(RpcResponse response) {
//		// biz process from response to t
//		System.out.println(JSON.toJSONString(response));
////		setData(response.getData());
//		latch.countDown();
//		return this;
//	}
	
	public T getResult() {
		try {
			latch.await();
		} catch (InterruptedException e) {
		}
		return getData();
	}

	@Override
	public void processResponse(T t) {
		setData(t);
		latch.countDown();
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public T get() throws InterruptedException {
        latch.await();
        return this.getData();
    }

    public T get(long timeout, TimeUnit unit)  {
        try {
            if (latch.await(timeout, unit)) {
                return this.getData();
            } else {
                throw new RuntimeException("async get time out");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("call future is interuptted", e);
        }
    }
    
    public void await() throws InterruptedException {
        latch.await();
    }

    /**
     * Waits for the CallFuture to complete without returning the result.
     * 
     * @param timeout the maximum time to wait.
     * @param unit the time unit of the timeout argument.
     * @throws InterruptedException if interrupted.
     * @throws TimeoutException if the wait timed out.
     */
    public void await(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        if (!latch.await(timeout, unit)) {
            throw new TimeoutException();
        }
    }

    public boolean isDone() {
        return latch.getCount() <= 0;
    }

	@Override
	public ClientCallback<T> filter(T respone) {
		return this;
	}


}

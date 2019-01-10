package com.lgz.grace.api.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ServiceUnavailableRetryStrategy;

/**
 * 针对请求地址可达，非200 响应码进行重试
 */
public class NoSuccessStatusRetryStrategy implements ServiceUnavailableRetryStrategy {
    private int executionCount;
    private long retryInterval;

    NoSuccessStatusRetryStrategy(Builder builder) {
        this.executionCount = builder.executionCount;
        this.retryInterval = builder.retryInterval;
    }

    /**
     * retry逻辑
     */
    @Override
    public boolean retryRequest(HttpResponse response, int executionCount, org.apache.http.protocol.HttpContext context) {
        if (response.getStatusLine().getStatusCode() != 200 && executionCount < this.executionCount)
            return true;
        else
            return false;
    }

    /**
     * retry间隔时间
     */
    @Override
    public long getRetryInterval() {
        return this.retryInterval;
    }

    public static final class Builder {
        private int executionCount;
        private long retryInterval;

        public Builder() {
            executionCount = 3;
            retryInterval = 1000;
        }

        public Builder executionCount(int executionCount) {
            this.executionCount = executionCount;
            return this;
        }

        public Builder retryInterval(long retryInterval) {
            this.retryInterval = retryInterval;
            return this;
        }

        public NoSuccessStatusRetryStrategy build() {
            return new NoSuccessStatusRetryStrategy(this);
        }
    }
}

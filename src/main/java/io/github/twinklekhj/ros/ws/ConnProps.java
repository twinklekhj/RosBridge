package io.github.twinklekhj.ros.ws;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ConnProps {
    @Builder.Default
    String host = "127.0.0.1";
    @Builder.Default
    int port = 9090;

    @Builder.Default
    boolean wait = false;
    @Builder.Default
    boolean printSendMsg = false;
    @Builder.Default
    boolean printReceivedMsg = false;
    @Builder.Default
    boolean printStackTrace = false;
    @Builder.Default
    int idleTimeout = 0;
    @Builder.Default
    int connectTimeout = 10000;
    @Builder.Default
    long stopTimeout = 0;

    public static ConnPropsBuilder builder(String host, int port) {
        return new ConnPropsBuilder().host(host).port(port);
    }

    public static ConnPropsBuilder builder(String host, int port, boolean wait) {
        return builder(host, port).wait(wait);
    }

    public static class ConnPropsBuilder {
        public ConnPropsBuilder port(int port) {
            if (port > 0 && port <= 65535) {
                this.port$value = port;
            } else throw new ArithmeticException("port range: 1~65535");
            return this;
        }
    }
}
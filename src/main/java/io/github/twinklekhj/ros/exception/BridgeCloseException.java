package io.github.twinklekhj.ros.exception;

public class BridgeCloseException extends RuntimeException {
    public BridgeCloseException(){
        super("RosBridge 연결이 해제되었습니다.");
    }
    public BridgeCloseException(String message){
        super(message);
    }
}

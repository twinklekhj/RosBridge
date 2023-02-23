package ros.op;

public enum CompressionType {
    NONE("none"), PNG("png"), CBOR("cbor"), CBOR_RAW("cbor-raw");
    public final String code;
    CompressionType(String code){
        this.code = code;
    }

    public String code(boolean flag) {
        if(flag){
            if(this.equals(CBOR) || this.equals(CBOR_RAW)){
                return NONE.code;
            }
        }
        return code;
    }

    public String code(){
        return code(false);
    }
}
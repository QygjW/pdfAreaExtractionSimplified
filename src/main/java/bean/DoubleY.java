package bean;

import lombok.Data;

@Data
public class DoubleY {
    private int ySource;
    private int yPicorTab;

    public DoubleY(){}

    public DoubleY(int ySource, int yPicorTab){
        this.ySource = ySource;
        this.yPicorTab = yPicorTab;
    }
}

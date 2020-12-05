package bean;

import lombok.Data;

@Data
public class CsvEntity {
    private String picOrTabTitile;
    private String pdfName;
    private String source;

    public CsvEntity(String picOrTabTitile,String pdfName,String source){
        this.picOrTabTitile = picOrTabTitile;
        this.pdfName = pdfName;
        this.source = source;
    }

    public String toRow(){
        return String.format("%s,%s,%s",this.picOrTabTitile,this.pdfName,this.source);
    }
}

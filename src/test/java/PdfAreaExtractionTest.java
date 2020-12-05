import org.junit.Test;
import utils.PdfBoxKeyWordPosition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfAreaExtractionTest {

    @Test
    public void testPdfBoxKeyWordPosition() throws IOException {
        PdfBoxKeyWordPosition pdfBoxKeyWordPosition = new PdfBoxKeyWordPosition();
        String filePath = "G://行业研究.pdf";
        File file = new File(filePath);
        String keyWord = "资料来源";
        List<Integer> resultList = pdfBoxKeyWordPosition.getKeyWordPosition(3, keyWord, file);
        for(int i = 0; i < resultList.size(); i++){
            System.out.println(resultList.get(i));
        }
    }

    @Test
    public void testEmptyArrayList(){
        List<Integer> alist = new ArrayList<Integer>();
        if(alist.isEmpty()){
            System.out.println("empty");
        }
    }

}

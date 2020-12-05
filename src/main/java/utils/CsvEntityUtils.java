package utils;

import bean.CsvEntity;

import java.util.ArrayList;
import java.util.List;

public class CsvEntityUtils {

    public static List<CsvEntity> writeCsvEntityList(String pdfName,List<String> picOrTabTitileList, List<String> sourceList){
        List<CsvEntity> csvEntities = new ArrayList();
        int picOrTabTitleListLength = picOrTabTitileList.size();
        int sourceListLength = sourceList.size();

        if(picOrTabTitleListLength < sourceListLength){
            for(int i = 0; i < picOrTabTitleListLength; i++){
                CsvEntity csvEntity = new CsvEntity(pdfName,picOrTabTitileList.get(i),sourceList.get(i));
                csvEntities.add(csvEntity);
            }
        }
        else if(picOrTabTitleListLength > sourceListLength){
            String lastSource = sourceList.get(sourceListLength-1);
            for(int i = 0; i < sourceListLength; i++){
                CsvEntity csvEntity = new CsvEntity(pdfName,picOrTabTitileList.get(i),sourceList.get(i));
                csvEntities.add(csvEntity);
            }
            for(int i = sourceListLength; i < picOrTabTitleListLength; i++){
                CsvEntity csvEntity = new CsvEntity(pdfName,picOrTabTitileList.get(i),lastSource);
                csvEntities.add(csvEntity);
            }
        }
        else{
            for(int i = 0; i < sourceListLength; i++){
                CsvEntity csvEntity = new CsvEntity(pdfName,picOrTabTitileList.get(i),sourceList.get(i));
                csvEntities.add(csvEntity);
            }
        }
        return csvEntities;
    }
}

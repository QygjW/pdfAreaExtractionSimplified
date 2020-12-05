package utils;

import bean.DoubleY;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 确定要截取的Rectangle的坐标
 */
public class AreaDetectionUtils {

    private final String pictureOrFormStartString = "(图|表|图表)"+"[ ]*"+"\\d+"+".*";
    private final String sourceString = ".*" + "来源" + ".*";
    private final String nameSeperator = "_";
    private Pattern pictureOrFormStartPattern;
    private Pattern sourcePattern;

    /**
     *
     * @param ySourceList
     * @param document
     * @param pts
     * @return
     */
    public List<DoubleY> getDoubleY(List<Integer> ySourceList, PDDocument document, PDFTextStripper pts, int pageNumber, File file, List<String> picOrTabTitleList, List<String> sourceList){
        List doubleYList = new ArrayList();
        if(ySourceList.isEmpty()){
            return doubleYList;
        }

        String content = null;
        try {
            content = pts.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PdfBoxKeyWordPosition pdfBoxKeyWordPosition = null;
        //初始化PdfBoxKeyWordPosition对象
        try {
            pdfBoxKeyWordPosition = new PdfBoxKeyWordPosition();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //得到字符串数组
        String[] stringArray = content.split("\r\n");
        //图表的正则匹配
        pictureOrFormStartPattern = Pattern.compile(pictureOrFormStartString);
        sourcePattern = Pattern.compile(sourceString);

        int lineNum = 0;
        for(;lineNum < stringArray.length;lineNum++) {
            Matcher pictureOrFormStartMatcher = pictureOrFormStartPattern.matcher(stringArray[lineNum]);
            Matcher sourceMatcher = sourcePattern.matcher(stringArray[lineNum]);

            int sourcePos = -1;
            List<Integer> yPicorTabList;
            if(sourceMatcher.matches()){
                sourceList.add(stringArray[lineNum]);
            }

            if (pictureOrFormStartMatcher.matches()) {
                picOrTabTitleList.add(stringArray[lineNum]);
                yPicorTabList = pdfBoxKeyWordPosition.getKeyWordPosition(pageNumber,stringArray[lineNum].substring(0,6),file);
                System.out.println(stringArray[lineNum]+"substring = "+stringArray[lineNum].substring(0,8));
                if(yPicorTabList.isEmpty()){
                    continue;
                }

                for(int i = 0; i < ySourceList.size(); i++){
                    if(ySourceList.get(i) <  yPicorTabList.get(0)){
                        sourcePos = ySourceList.get(i);
                        break;
                    }
                }
                if(sourcePos != -1){
                    DoubleY dy = new DoubleY(sourcePos,yPicorTabList.get(0));
                    doubleYList.add(dy);
                }
            }
        }
        return doubleYList;
    }
}

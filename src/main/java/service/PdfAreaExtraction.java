package service;

import bean.DoubleY;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import utils.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PdfAreaExtraction {

    private static final String pathSeperator = "/";

    private static final int pdfMaxHeight = 800;

    /**
     * 读入pdf文件地址，输出解析后的txt文件
     * @param filePath
     * @param targetPath
     * @throws IOException
     */
    public static void extractWords(String filePath, String targetPath) throws IOException {
        try {
            // 开始提取页数
            int startPage = 0;
            // 结束提取页数
            int endPage = Integer.MAX_VALUE;
            String content = null;
            File file = new File(filePath);

            //获取该文件夹下的所有文件，并处理
            File[] files=file.listFiles();
            IllegalCharInNameHandler illegalCharInNameHandler = new IllegalCharInNameHandler();
            for(File tfile:files){
                String totalName = tfile.getName();
                int dotPosition = totalName.lastIndexOf('.');
                String pdfName = totalName.substring(0,dotPosition);

                System.out.println("当前文件名："+pdfName);
                //在targetPath下创建pdfName文件夹
                String pdfDirPath = targetPath + pathSeperator + illegalCharInNameHandler.legalizeName(pdfName);
                File pdfDir = new File(pdfDirPath);
                if(!pdfDir.exists()){
                    pdfDir.mkdir();
                }

                PDDocument document = PDDocument.load(tfile);
                PDFTextStripper pts = new PDFTextStripper();
                startPage = 1;
                endPage = document.getNumberOfPages();

                PdfBoxKeyWordPosition pdfBoxKeyWordPosition = new PdfBoxKeyWordPosition();

                //对每个页面进行处理
                int pictureCount = 1;
                for(int currentPage = startPage; currentPage <= endPage; currentPage++){
                    pts.setStartPage(currentPage);
                    pts.setEndPage(currentPage);

                    /*
                     * 首先获取全部“资料来源”的y坐标List
                     */
                    String keyWords = "资料来源";
                    List<Integer> keyWordsList = pdfBoxKeyWordPosition.getKeyWordPosition(currentPage,keyWords,tfile);
                    /*
                     * 获取“资料来源”和“图表标题”的y坐标对象列表
                     */
                    AreaDetectionUtils areaDetectionUtils = new AreaDetectionUtils();
                    List<DoubleY> doubleYList = areaDetectionUtils.getDoubleY(keyWordsList,document,pts,currentPage,tfile);
                    if(!doubleYList.isEmpty()){

                        for(DoubleY doubleY:doubleYList){
                            makePictureFromPdf(doubleY,tfile,pdfDirPath,pictureCount,currentPage-1);
                            pictureCount++;
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makePictureFromPdf(DoubleY doubleY,File tfile, String targetDirPath,int count,int currentPage) throws IOException {

        String completedTargetPath = targetDirPath + pathSeperator + count + ".png";
        int ySourceLabel = doubleY.getYSource();
        int yPicorTabLabel = doubleY.getYPicorTab();
//        String pdfPath = "G:\\tempF/testF4.pdf";
        int aimedY = pdfMaxHeight - yPicorTabLabel;
        int height = yPicorTabLabel - ySourceLabel;

        Rectangle imgRect = new Rectangle(20,aimedY-12,560,height+32);
        BufferedImage bufImage = PdxImageUtils.readRectangelImage(tfile, currentPage, imgRect);

        File outputfile = new File(completedTargetPath);
        ImageIO.write(bufImage, "png", outputfile);
    }

    public static void main(String[] args) throws IOException {
        String filePath = "G://pdfs";
        String targetPath = "G://p";
        extractWords(filePath,targetPath);
    }
}

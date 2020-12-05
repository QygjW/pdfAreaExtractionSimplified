package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 继承 pdfbox 中 PDFTextStripper类，获取关键字坐标
 */
public class PdfBoxKeyWordPosition extends PDFTextStripper {

    //关键字字符数组
    private char[] key;

    // 当前页坐标集合
    private List<Map<String, Object>> pageList = new ArrayList();

    //使用二进制数据
    public PdfBoxKeyWordPosition() throws IOException {
        super();
        super.setSortByPosition(true);
    }

    /**
     * @param pageNumber 页数
     * @return
     */
    public List<Map<String, Object>> getCoordinate(int pageNumber, byte[] bytes) {

        List<Map<String, Object>> coordinates = new ArrayList();
        try {
            document = PDDocument.load(bytes); //文件二进制数据

            super.setSortByPosition(true);

            super.setStartPage(pageNumber);

            super.setEndPage(pageNumber);

            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());

            super.writeText(document, dummy);

            coordinates.addAll(pageList);

            pageList.clear();

        } catch (Exception e) {
        } finally {

            pageList.clear();

            try {
                if (document != null) {
                    document.close();
                }
            } catch (IOException e) {
            }
        }
        return coordinates;
    }

    // 获取坐标信息
    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {

        for (int i = 0; i < textPositions.size(); i++) {

            String str = textPositions.get(i).getUnicode();

            //找到 key 中第一位所在位置
            if (str.equals(String.valueOf(key[0]))) {

                int count = 0;

                for (int j = 0; j < key.length; j++) {

                    String s = "";

                    try {
                        s = textPositions.get(i + j).getUnicode();
                    } catch (Exception e) {
                        s = "";
                    }

                    //判断key 中每一位是否和文本中顺序对应，一旦不等说明 关键字与本段落不等，则停止本次循环
                    if (s.equals(String.valueOf(key[j]))) {
                        count++;
                    } else if (count > 0) {
                        break;
                    }
                }

                //判断 key 中字 在文本是否连续，是则获取坐标
                if (count == key.length) {

                    Map<String, Object> coordinate = new HashMap();

                    TextPosition tp = textPositions.get(i);

                    // X坐标 在这里加上了字体的长度，也可以直接 tp.getX()
                    Float x = tp.getX() + tp.getFontSize();

                    // Y坐标 在这里减去的字体的长度，也可以直接 tp.getPageHeight() - tp.getY()
                    Float y = tp.getPageHeight() - tp.getY() - 4 * tp.getFontSize();

                    coordinate.put("x", x);
                    coordinate.put("y", y);

                    pageList.add(coordinate);
                }
            }
        }
    }

    /**
     * @return
     * @function 获取KeyWord的y坐标List
     */
    public List<Integer> getKeyWordPosition(int pageNumber, String keyWord, File file) {

        try {

            char[] key = new char[keyWord.length()];
            for (int i = 0; i < keyWord.length(); i++) {
                key[i] = keyWord.charAt(i);
            }
            this.key = key;

            InputStream is = null;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {

                is = new FileInputStream(file);

                byte[] buffer = new byte[is.available()];

                Integer n = 0;

                while ((n = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, n);
                }

            } catch (IOException e) {

                e.printStackTrace();
            } finally {

                try {
                    bos.close();

                    if (is != null) {
                        is.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            byte[] bytes = bos.toByteArray();

            List<Map<String, Object>> wordsPcoordinates = getCoordinate(pageNumber, bytes);

            List<Integer> resultList = new ArrayList();

            for (Map<String, Object> map : wordsPcoordinates) {
//                System.out.println("x坐标 -> " + map.get("x"));
//                System.out.println("y坐标 -> " + map.get("y"));
                resultList.add(((Float)map.get("y")).intValue());
            }

            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
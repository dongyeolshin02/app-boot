package it.korea.app_boot.common.files;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtils {

    @Value("${server.file.upload.path}")
    private String filePath;

   /** 
    * 파일 업로드 기능 
   */
    public Map<String, Object>  uploadFiles(MultipartFile file,  String type) throws Exception{ 
        Map<String, Object>   resultMap = new HashMap<>();

        if(file == null || file.isEmpty()) {
            return null;
        }

        String fileName = file.getOriginalFilename();
        String extention = fileName.substring(fileName.lastIndexOf(".")+1) ;
        String randName = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        String storedFileName = randName + "." + extention;

        String fullPath = filePath + type  + File.separator + storedFileName;
        File newFile = new File(fullPath);

        //경로없으면 만들어주자
        if( !newFile.getParentFile().exists()) {
            //경로 만들어주기
            newFile.getParentFile().mkdirs();
        }

        newFile.createNewFile(); //빈파일
        file.transferTo(newFile);

        resultMap.put("fileName", fileName);
        resultMap.put("storedFileName", storedFileName);
        resultMap.put("filePath", filePath);

        return resultMap;
    }


    /**
     * 파일 삭제 
     */
    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);

        if(deleteFile.exists()) {
            deleteFile.delete();
        }
    }
}

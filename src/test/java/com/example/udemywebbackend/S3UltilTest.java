package com.example.udemywebbackend;

import com.example.udemywebbackend.admin.Upload.AWS.AmazoneS3Util;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class S3UltilTest {

    @Test
    public void testListFolder(){
        String folderName="test/class.png";
    }

    @Test
    public void UploadFile() throws FileNotFoundException {
        String folderName="test";
        String fileName="hello1.png";
        String filePath="D:\\BACKUP\\MEME\\vohinh.png";

        String ext1= FilenameUtils.getExtension(filePath);
        System.out.println(ext1);
        InputStream inputStream=new FileInputStream(filePath);

//        AmazoneS3Util.uploadFile(folderName,fileName,inputStream);
    }
}

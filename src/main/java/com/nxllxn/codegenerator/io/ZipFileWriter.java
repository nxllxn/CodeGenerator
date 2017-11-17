package com.nxllxn.codegenerator.io;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 用于将生成的代码文件压缩为ZIP文件并写入到输出流中
 *
 * @author wenchao
 */
public class ZipFileWriter extends FileWriter {
    /**
     * 待写入输出流
     */
    private OutputStream outputStream;

    /**
     * 项目名称，用于组建压缩文件一级目录
     */
    private String projectName;

    /**
     * SLF4J+LOG4J
     */
    private Logger logger = LoggerFactory.getLogger(ZipFileWriter.class);

    public ZipFileWriter(List<GeneratedFile> generatedFiles, String projectName, OutputStream outputStream) {
        super(generatedFiles);
        this.projectName = projectName;
        this.outputStream = outputStream;
    }

    @Override
    public void write() {
        if (generatedFiles == null || generatedFiles.isEmpty()){
            return;
        }

        ZipOutputStream zipOutputStream = null;

        try {
            zipOutputStream = new ZipOutputStream(outputStream);

            byte[] buffer = new byte[1024];
            ZipEntry zipEntry;
            InputStream byteArrayInputStream;
            for (GeneratedFile generatedFile : generatedFiles) {
                zipEntry = new ZipEntry(projectName + File.separator + generatedFile.getRelativeFilePath());

                zipOutputStream.putNextEntry(zipEntry);

                byteArrayInputStream = new ByteArrayInputStream(generatedFile.getFormattedContent().getBytes());

                int length;
                while ((length = byteArrayInputStream.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, length);
                }
            }

            zipOutputStream.flush();
        } catch (Exception e) {
            logger.error("写入生成代码到指定输出流失败：{}", e.getMessage());
        } finally {
            try {
                if (zipOutputStream != null) {
                    zipOutputStream.close();
                }
            } catch (IOException ok) {
                //it is safe to go
            }
        }
    }
}

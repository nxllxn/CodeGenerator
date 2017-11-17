package com.nxllxn.codegenerator.io;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * 本地磁盘文件代码写入工具
 *
 * @author wenchao
 */
public class ProjectFileWriter extends FileWriter {
    /**
     * SLF4j+LOG4J
     */
    private Logger logger = LoggerFactory.getLogger(ProjectFileWriter.class);

    public ProjectFileWriter(List<GeneratedFile> generatedFiles) {
        super(generatedFiles);
    }

    @Override
    public void write() {
        if (generatedFiles == null || generatedFiles.isEmpty()){
            return;
        }

        File localFile;
        for (GeneratedFile generatedFile:generatedFiles){
            try {
                localFile = generatedFile.getWriteToFile();

                localFile.getParentFile().mkdirs();
                localFile.createNewFile();

                FileOutputStream fileOutputStream = new FileOutputStream(localFile, false);
                OutputStreamWriter outputStreamWriter;
                if (generatedFile.getFileEncoding() == null) {
                    outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                } else {
                    outputStreamWriter = new OutputStreamWriter(fileOutputStream, generatedFile.getFileEncoding());
                }

                BufferedWriter bw = new BufferedWriter(outputStreamWriter);
                bw.write(generatedFile.getFormattedContent());
                bw.flush();
                bw.close();
            }catch (Exception e){
                logger.error("抱歉，写入文件失败:{}",e.getMessage());
            }
        }
    }
}

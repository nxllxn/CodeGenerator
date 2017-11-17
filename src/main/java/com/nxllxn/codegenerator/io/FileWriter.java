package com.nxllxn.codegenerator.io;


import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;

import java.util.List;

public abstract class FileWriter {
    protected List<GeneratedFile> generatedFiles;

    public FileWriter(List<GeneratedFile> generatedFiles) {
        this.generatedFiles = generatedFiles;
    }

    public abstract void write();
}

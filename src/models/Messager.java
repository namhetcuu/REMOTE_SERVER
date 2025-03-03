package models;

import java.io.Serializable;

public class Messager implements Serializable {
	private static final long serialVersionUID = 1L;
	private String text;
    private Object object;
    private Object object2;
    private byte[] fileContent; // Dữ liệu file
    private String fileName; // Tên file

    public Messager() {
        this.text = "";
    }

    public Messager(String text) {
        this.text = text;
    }
    
    public Messager(String text, Object object) {
        this.text = text;
        this.object = object;
    }
    
    public Messager(String text, Object object, Object object2) {
        this.text = text;
        this.object = object;
        this.object2 = object2;
    }
    
    public Messager(String text, Object object2, byte[] fileContent, String fileName) {
        this.text = text;
        this.object2 = object2;
        this.fileContent = fileContent;
        this.fileName = fileName;
    }

    public String toString() {
        return "Messager[\n" +
                "\ttext='" + text + '\'' + '\n' +
                ((object != null) ? "\tObject=" + object + '\n' : "") +
                ((object != null) ? "\tObject2=" + object2 + '\n' : "") +
                ']';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

	public Object getObject() {
		return object;
	}

	public Object getObject2() {
		return object2;
	}

	public void setObject2(Object object2) {
		this.object2 = object2;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	public byte[] getFileContent() {
        return fileContent;
    }

    public String getFileName() {
        return fileName;
    }
}

public class MyFiles {
	private int id;
	private String name;
	private byte[] data;
	private String fileExtension;
	
	public MyFiles(int id,String name, byte[] data, String fileExtension) {
		this.setId(id);
		this.setName(name);
		this.setData(data);
		this.setFileExtension(fileExtension);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	

}

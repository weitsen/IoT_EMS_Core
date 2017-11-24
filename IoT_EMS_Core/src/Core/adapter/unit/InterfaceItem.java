package Core.adapter.unit;

public class InterfaceItem {
	// 一個監控 就是一個介面(以元件單位為基礎)(EX:滑鼠有 左鍵 右鍵 滾輪 3種監控)
	private int InterfaceItem_id;
	private String InterfaceItem_type;
	private String InterfaceItem_name;
	
	//--------------------------------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------------------------------
	public InterfaceItem(){
	}
	
	//--------------------------------------------------------------------------------------------------------
	// get / set
	//--------------------------------------------------------------------------------------------------------
	
	public int getInterfaceItem_id() {
		return InterfaceItem_id;
	}
	public void setInterfaceItem_id(int interfaceItem_id) {
		InterfaceItem_id = interfaceItem_id;
	}
	public String getInterfaceItem_type() {
		return InterfaceItem_type;
	}
	public void setInterfaceItem_type(String interfaceItem_type) {
		InterfaceItem_type = interfaceItem_type;
	}
	public String getInterfaceItem_name() {
		return InterfaceItem_name;
	}
	public void setInterfaceItem_name(String interfaceItem_name) {
		InterfaceItem_name = interfaceItem_name;
	}
}

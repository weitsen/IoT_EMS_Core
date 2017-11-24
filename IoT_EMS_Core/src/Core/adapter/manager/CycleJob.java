package Core.adapter.manager;

import java.lang.reflect.Method;

import Core.task.task.unit.PeriodicSystemTask;

public class CycleJob extends PeriodicSystemTask {
	private int node_id;
	private Object obj = null;
	private String MethodName = null;
	private Method m = null;
	
	// CycleJob  週期性工作
	public CycleJob() {
		
	}
	
	public CycleJob( Object object , String method_name ) {
		obj = object;
		MethodName = method_name;
	}
	
	//--------------------------------------------------------------------------------------------------------
	// get / set
	//--------------------------------------------------------------------------------------------------------
	public int getNode_id() {
		return node_id;
	}

	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}
	
	public String getMethodName() {
		return MethodName;
	}

	public void setMethodName(String methodName) {
		MethodName = methodName;
	}
	
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	//--------------------------------------------------------------------------------------------------------
	// 
	//--------------------------------------------------------------------------------------------------------
	
	// Overwirte
	public void TaskRun() {
		try {
			Class c = obj.getClass();
			Method m = c.getMethod( MethodName , null );
			m.invoke( obj , null );
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}

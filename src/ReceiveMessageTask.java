import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ReceiveMessageTask implements Callable<String>{

	@Override
	public String call() throws Exception {
		
		if(true){
			throw new Exception("inside future comp");
		}
		
		return "success";
	}
	
	public static void main(String[] args){
		
		ReceiveMessageTask recv = new ReceiveMessageTask();
	
		ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(recv);
        try {
			System.out.println(future.get());
		} catch (InterruptedException e) {
			System.out.println("interrupted");
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.out.println("execution");
			e.printStackTrace();
		}
        
	}

}



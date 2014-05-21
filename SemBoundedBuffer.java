package contentsharing;





public class SemBoundedBuffer extends BoundedBuffer {
    Semaphore emptySlots, fullSlots;

    public SemBoundedBuffer (int maxSize) {
        super(maxSize);
        emptySlots = new Semaphore(maxSize);
        fullSlots  = new Semaphore(0);
    }

    public int getSize(){
    	return fullSlots.count;
    }

    
    public Object get() throws InterruptedException{
        Object value;
        	fullSlots.acquire();
        	value = super.get();
        	//System.out.println("OBJECT "+value+" TAKEN FROM THE BUFFER");
            emptySlots.release();
      return value;
    }


    public void put(Object value) throws InterruptedException {
		emptySlots.acquire();
    		//System.out.println("OBJECT "+value+" PUT IN THE BUFFER");
            super.put(value);
        	fullSlots.release();

    }
}
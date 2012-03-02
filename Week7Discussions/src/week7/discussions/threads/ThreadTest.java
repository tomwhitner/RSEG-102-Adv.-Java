package week7.discussions.threads;

public class ThreadTest {
	
	public static void main(String[] args) {
		new ThreadTest().main(); // need instance to use inner class
	}

	public void main() {

		Runnable r1 = new Counter("One");
		Runnable r2 = new Counter("Two");

		Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				System.out.println("Uncaught thread exception: \n Thread = " + thread + "\n Throwable = " + throwable);
			}
			
		});
		
		Thread t1 = new Thread(r1);

		Thread t2 = new Thread(r2);


		System.out.println("Starting thread 1...");
		t1.start();
		System.out.println("Starting thread 2...");
		t2.start();
		System.out.println("Main done.");

	}

	public static final int STEPS = 5;
	public static final int DELAY = 500;

	private class Counter implements Runnable {

		private final String name;

		@Override
		public String toString() {
			return "Counter [name=" + name + "]";
		}

		public Counter(String name) {
			this.name = name;
		}

		@Override
		public void run() {

			try {
				for (int i = 1; i <= STEPS; i++) {
					System.out.println(name + ": " + i);
					if (i==4) {
						throw new UnsupportedOperationException("Test");
					}
					Thread.sleep(DELAY);
				}
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}

	}
}

package br.com.armange.commons.thread;

import static br.com.armange.commons.thread.ThreadUtil.sleepUnchecked;
import static br.com.armange.commons.thread.TryAsyncBuilder.tryAsync;
import static java.lang.System.out;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.junit.Test;
import org.mockito.Mockito;

public class TryAsyncBuilderTest {
    private static class RunnableWithDeay implements Runnable {
        private final long delay;
        
        private RunnableWithDeay(final long delay) {
            this.delay = delay;
        }

        @Override
        public void run() {
            sleepUnchecked(delay);
            
            out.println("RunnableWithDeay class was executed successfully");
        }
        
    }
    
    private static class RunnableWithDeayAndException implements Runnable {
        private final long delay;
        
        private RunnableWithDeayAndException(final long delay) {
            this.delay = delay;
        }

        @Override
        public void run() {
            System.out.println("An exception will be thrown!");
            
            sleepUnchecked(delay);
            
            throw new RuntimeException("This is a expected esception.");
        }
        
    }
    
    private static class SimpleRunnable implements Runnable {
        @Override
        public void run() {
            out.println("SimpleRunnable class was executed successfully");
        }
    }
    
    private static class ExceptionConsumer implements Consumer<Throwable> {
        @Override
        public void accept(final Throwable throwable) {
            out.println(throwable.getClass().getName() + " -> This Exception was expected.");
        }
    }
    

    @Test
    public void tryWithoutExceptions() {
        final RunnableWithDeay runnableWithDeay = spy(new RunnableWithDeay(500));
        final SimpleRunnable simpleRunnable = spy(new SimpleRunnable());
        
        tryAsync(runnableWithDeay).execute();
        
        simpleRunnable.run();
        
        verify(runnableWithDeay, times(0)).run();
        verify(simpleRunnable).run();
        
        sleepUnchecked(2000);
        
        verify(runnableWithDeay).run();
    }
    
    @Test
    public void tryWithExceptions() {
        final RunnableWithDeayAndException runnableWithDeayAndException = spy(new RunnableWithDeayAndException(500));
        final SimpleRunnable simpleRunnable = spy(new SimpleRunnable());
        final ExceptionConsumer exceptionConsumer = spy(new ExceptionConsumer());
        
        tryAsync(runnableWithDeayAndException)
            .addCatcher(RuntimeException.class, exceptionConsumer)
            .execute();
        
        simpleRunnable.run();
        
        verify(runnableWithDeayAndException, times(0)).run();
        verify(simpleRunnable).run();
        
        sleepUnchecked(3000);
        
        verify(runnableWithDeayAndException).run();
        verify(exceptionConsumer).accept(any());
    }
    
    @Test
    public void tryWithExceptionsAndFinalizer() {
        final RunnableWithDeayAndException runnableWithDeayAndException = spy(new RunnableWithDeayAndException(500));
        final SimpleRunnable simpleRunnable = spy(new SimpleRunnable());
        final ExceptionConsumer exceptionConsumer = spy(new ExceptionConsumer());
        
        tryAsync(runnableWithDeayAndException)
            .addCatcher(RuntimeException.class, exceptionConsumer)
            .finallyAsync(simpleRunnable)
            .execute();
        
        simpleRunnable.run();
        
        verify(runnableWithDeayAndException, times(0)).run();
        verify(simpleRunnable).run();
        
        sleepUnchecked(3000);
        
        verify(runnableWithDeayAndException).run();
        verify(exceptionConsumer).accept(any());
        verify(simpleRunnable, times(2)).run();
    }
}

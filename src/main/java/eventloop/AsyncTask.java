package eventloop;

import java.util.concurrent.ExecutorService;

public class AsyncTask implements ITask{
    private final ExecutorService executorService;
    private final Task task;

    public AsyncTask(ExecutorService executorService, Task task) {
        this.executorService = executorService;
        this.task = task;
    }

    @Override
    public void execute() {
        executorService.execute(task::execute);
    }
}

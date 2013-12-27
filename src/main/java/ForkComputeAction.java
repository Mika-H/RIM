import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

public class ForkComputeAction extends RecursiveAction {
    final static int threshold = 100;
    protected static int countParsedPages = 0;

    ArrayList<String> arr;
    int start;
    int end;

    public ForkComputeAction (ArrayList<String> arrList, int start, int end) {
        this.arr = arrList;
        this.start = start;
        this.end = end;
    }

    protected void compute() {
        if ((end - start) < threshold) {

            for (int i = start; i < end; i++) {
                System.out.println("передаємо: " + (arr.get(i)));
                String STemp = arr.get(i);
                DomenParse.getParseClass(STemp).parseAll();
                countParsedPages++;
            }

        } else {
            int middle = (start + end) / 2;
            invokeAll(new ForkComputeAction(arr, start, middle), new ForkComputeAction(arr, middle, end));
        }
    }
}
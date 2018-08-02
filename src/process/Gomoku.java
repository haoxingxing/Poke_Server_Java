package process;

public class Gomoku  {
    matchqueue queue=new matchqueue("gomoku",2);
    people t;
    public Gomoku(people t)
    {
        this.t=t;
    }
    public void joinqueue()
    {
        queue.addtoqueue(t.username);
    }
    public boolean ismatched()
    {
        return queue.ismatched(t.username);
    }

}

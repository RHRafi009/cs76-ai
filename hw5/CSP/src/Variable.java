import java.util.LinkedList;

/**
 * Created by JackGuan on 2/24/14.
 */
public class Variable implements Comparable<Variable> {
    protected int id;
    protected int assignment;
    protected int degree;
    protected LinkedList<Domain> domains;

    Variable(int i){
        id = i;
    }

    Variable(int i, int ass){
        id = i;
        assignment = ass;
    }

    Variable(Variable v){
        id = v.id;
        assignment = v.assignment;
        degree = v.degree;
        domains = (LinkedList<Domain>) v.domains.clone();
    }

    Variable(int i, LinkedList<Domain> d, int ass){
        id = i;
        domains = d;
        assignment = ass;
    }

    public int domainSize(){
        return domains.size();
    }

    public void assign(Domain domain){
        assignment = domain.d;
    }

    public void undoAssign(){
        assignment = -1;
    }

    public int getAssignment(){
        return assignment;
    }

    public LinkedList<Domain> getDomains(){
        return domains;
    }

    public int getId(){
        return id;
    }

    public int getDegree(){
        return degree;
    }

    public void setDegree(int d){
        degree = d;
    }

    @Override
    public int compareTo(Variable o) {
        int compared = (int) Math.signum(domains.size() - o.domains.size());
        if(compared != 0)
            // return the one with minimum remaining values
            return compared;
        else
            // return the one with maximum degree
            return (int) Math.signum(o.degree - degree);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "[" + id + "," + assignment + "]";
    }

    @Override
    public boolean equals(Object other) {
        return id == ((Variable) other).id;
    }
}

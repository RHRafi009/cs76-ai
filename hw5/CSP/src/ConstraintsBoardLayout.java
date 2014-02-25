import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/24/14.
 */
public class ConstraintsBoardLayout extends Constraints{
    // define the 6 basic relationship
    public static final int EQ = 0, GE = 1, LE = 2, GT = 3, LT = 4, NE = 5;

    ConstraintsBoardLayout(){
        ConsArc = new HashMap<>();
        adjacents = new HashMap<>();
    }

    /**
     * addConstraint
     * <p/>
     * Build the constraint graph based on variable name and
     * relationship. Remember to build an in-directed graph,
     * so you need to call this twice every time
     *
     * @param var1       the first variable
     * @param relatinshp the relationship
     * @param var2       the second variable
     */
    public void addConstraint(Variable var1, Variable var2) {
        // build the graph
        if(!adjacents.containsKey(var1)) adjacents.put(var1, new LinkedList<Variable>());
        adjacents.get(var1).add(var2);
        return;
    }

    public boolean isSatisfied(Variable var1, Variable var2){
        ArcPair varpair = new ArcPair(var1, var2);
        if (ConsArc.containsKey(varpair)) {
            int r = ConsArc.get(varpair);
            switch (r) {
                case EQ:
                    return var1.assignment == var2.assignment;
                case GE:
                    return var1.assignment >= var2.assignment;
                case LE:
                    return var1.assignment <= var2.assignment;
                case GT:
                    return var1.assignment > var2.assignment;
                case LT:
                    return var1.assignment < var2.assignment;
                case NE:
                    return var1.assignment != var2.assignment;
                default:
                    break;
            }
        }
        // System.out.println("No constraint between " + varpair);
        return true;
    }

    @Override
    public boolean conflictTest(LinkedList<Variable> vars) {
        return false;
    }
}

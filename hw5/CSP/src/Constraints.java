import com.sun.jndi.url.iiop.iiopURLContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/23/14.
 */
public class Constraints {
    // binary contraint, map from a pair of variable to a relationship
    public HashMap<VarPair, Integer> binary = null;
    // contraint graph
    public HashMap<Variable, LinkedList<Variable>> adjacents = null;

    public void addConstraint(Variable var1, String relatinshp, Variable var2) {

    }

    public boolean isSatisfied(Variable var1, Variable var2) {
        return false;
    }

    public boolean conflictTest(LinkedList<Variable> vars, Variable var) {
        return false;
    }

    public boolean conflictTest(LinkedList<Variable> vars) {
        return false;
    }

    public class VarPair {
        Variable first;
        Variable second;

        VarPair(Variable v1, Variable v2) {
            first = v1;
            second = v2;
        }

        @Override
        public int hashCode() {
            return first.id * Config.VAR_NUM_SUPRT + second.id;
        }

        @Override
        public String toString() {
            return "[" + first + "," + second + "]";
        }

        @Override
        public boolean equals(Object other) {
            return first == ((VarPair) other).first
                    && second == ((VarPair) other).second;
        }
    }
}

package simplf;
 
import java.util.List;

class SimplfFunction implements SimplfCallable {

    private final Stmt.Function declaration;
    private Environment closure;

    SimplfFunction(Stmt.Function declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
        // throw new UnsupportedOperationException("TODO: implement functions");
    }

    public void setClosure(Environment environment) {
        this.closure = environment;
        // throw new UnsupportedOperationException("TODO: implement functions");
    }

    public int arity() {
        return declaration.params.size();  // ✅ Needed to fix the error
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        Environment localEnv = closure;
    
        // Step 1: bind parameters
        for (int i = 0; i < declaration.params.size(); i++) {
            localEnv = localEnv.define(
                declaration.params.get(i),
                declaration.params.get(i).lexeme,
                args.get(i)
            );
        }
    
        Object result = null;
    
        // Step 2: evaluate function body statements using localEnv
        for (int i = 0; i < declaration.body.size(); i++) {
            Stmt stmt = declaration.body.get(i);
    
            if (i == declaration.body.size() - 1 && stmt instanceof Stmt.Expression) {
                result = interpreter.evaluateInEnv(((Stmt.Expression) stmt).expr, localEnv);
            } else if (stmt instanceof Stmt.Var) {
                Object value = interpreter.evaluate(((Stmt.Var) stmt).initializer);
                localEnv = localEnv.define(((Stmt.Var) stmt).name, ((Stmt.Var) stmt).name.lexeme, value);
            } else if (stmt instanceof Stmt.Function) {
                Stmt.Function func = (Stmt.Function) stmt;
                localEnv = localEnv.define(func.name, func.name.lexeme, null);
                SimplfFunction fn = new SimplfFunction(func, localEnv);
                localEnv.assign(func.name, fn);
            } else {
                interpreter.executeInEnv(stmt, localEnv);
            }
        }
    
        return result;
    }
    
    

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }

}
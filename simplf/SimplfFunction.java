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
        Environment functionEnv = new Environment(closure);

        // Bind parameters
        for (int i = 0; i < declaration.params.size(); i++) {
            functionEnv = functionEnv.define(
                declaration.params.get(i),
                declaration.params.get(i).lexeme,
                args.get(i)
            );
        }

        // Save previous environment and switch to function environment
        Environment previous = interpreter.globals;
        try {
            interpreter.globals = functionEnv;
            for (Stmt stmt : declaration.body) {
                interpreter.interpret(List.of(stmt));
            }
        } finally {
            interpreter.globals = previous;
        }

        return null;
        // throw new UnsupportedOperationException("TODO: implement functions");
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }

}
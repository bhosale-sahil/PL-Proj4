package simplf;

import java.util.List;

class SimplfFunction implements SimplfCallable {
    private final Stmt.Function declaration;
    private Environment closure;

    SimplfFunction(Stmt.Function declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    public void setClosure(Environment env) {
        this.closure = env;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        Environment localEnv = new Environment(closure);

        for (int i = 0; i < declaration.params.size(); i++) {
            Token param = declaration.params.get(i);
            localEnv = localEnv.define(param, param.lexeme, args.get(i));
        }

        Environment previous = interpreter.getEnvironment();
        interpreter.setEnvironment(localEnv);
        Object result = null;

        try {
            for (Stmt stmt : declaration.body) {
                result = interpreter.runInCurrentEnv(stmt);
            }
        } finally {
            interpreter.setEnvironment(previous);
        }

        return result;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }
}

package simplf;

class Environment {
    final AssocList assocList;
    final Environment enclosing;

    Environment() {
        this.assocList = null;
        this.enclosing = null;
    }

    Environment(Environment enclosing) {
        this.assocList = null;
        this.enclosing = enclosing;
    }

    Environment(AssocList assocList, Environment enclosing) {
        this.assocList = assocList;
        this.enclosing = enclosing;
    }

    Environment define(Token varToken, String name, Object value) {
        AssocList newList = new AssocList(name, value, this.assocList);
        return new Environment(newList, this.enclosing);
    }

    void assign(Token name, Object value) {
        for (AssocList cursor = this.assocList; cursor != null; cursor = cursor.next) {
            if (cursor.name.equals(name.lexeme)) {
                cursor.value = value;
                return;
            }
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
        } else {
            throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'");
        }
    }

    Object get(Token name) {
        for (AssocList cursor = this.assocList; cursor != null; cursor = cursor.next) {
            if (cursor.name.equals(name.lexeme)) {
                return cursor.value;
            }
        }
        if (enclosing != null) {
            return enclosing.get(name);
        }
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'");
    }
}

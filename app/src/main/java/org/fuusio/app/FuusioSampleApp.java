package org.fuusio.app;

import org.fuusio.api.app.FuusioApplication;
import org.fuusio.api.dependency.ApplicationScope;

/*
import com.floxp.library.components.Calculator;
import com.floxp.runtime.Addition;
import com.floxp.runtime.Code;
import com.floxp.runtime.CompiledType;
import com.floxp.runtime.DynamicMethod;
import com.floxp.runtime.DynamicType;
import com.floxp.runtime.Executable;
import com.floxp.runtime.ExecutionContext;
import com.floxp.runtime.Instance;
import com.floxp.runtime.Literal;
import com.floxp.runtime.Method;
import com.floxp.runtime.MethodInvocation;
import com.floxp.runtime.Multiplication;
import com.floxp.runtime.Property;
import com.floxp.runtime.PropertyInvocation;
import com.floxp.runtime.Return;
import com.floxp.runtime.Statement;
import com.floxp.runtime.Subtraction;
import com.floxp.runtime.VirtualMachine;
import com.floxp.runtime.type.MutableInteger;
import com.floxp.runtime.type.MutableString;
*/

public class FuusioSampleApp extends FuusioApplication {

    // Google Analytics Property ID
    public final static int PROPERTY_ID = 0; // TODO

    public final static String DATABASE_NAME = "com.floxp.name.DB";
    public final static String PATH_FLOXP = "floxp";
    public final static String PATH_MODELS = PATH_FLOXP + "/models";
    public final static String PATH_RESOURCES = PATH_FLOXP + "/resources";
    public final static String PATH_RESOURCES_FONTS = PATH_RESOURCES + "/fonts";
    public final static String PATH_RESOURCES_ICONS = PATH_RESOURCES + "/icons";
    public final static String PATH_RESOURCES_IMAGES = PATH_RESOURCES + "/images";
    public final static String PATH_RESOURCES_NINEPATCHES = PATH_RESOURCES + "/resources";
    public final static String PATH_RESOURCES_SOUNDS = PATH_RESOURCES + "/sounds";

    public FuusioSampleApp() {
        super();
    }

    @Override
    protected ApplicationScope createDependencyScope() {
        return new FuusioSampleAppScope(this);
    }

    /**
     * Gets the Google Analytics Property ID.
     *
     * @return The property ID as an {@code int} value.
     */
    public int getPropertyId() {
        return PROPERTY_ID;
    }

    /**
     *
     *
     private void doTests() {
     final Calculator calculator = new Calculator();
     final CompiledType calculatorType = new CompiledType(Calculator.class);
     final VirtualMachine vm = new VirtualMachine(this);
     final ExecutionContext ctx = new ExecutionContext(vm);
     final Method addDigitMethod = calculatorType
     .getMethod(Calculator.M_receiveDigit_int);
     ctx.mArgs = new Object[] { 8 };
     ctx.mContextObject = calculator;
     final Object returnValue = addDigitMethod.execute(ctx);

     final DynamicType helloWorldType = new DynamicType("HelloWorld");
     final Property intProperty = new Property("x", MutableInteger.TYPE);
     final Property stringProperty = new Property("string", MutableString.TYPE);
     final Property[] properties = { intProperty, stringProperty };
     helloWorldType.setProperties(properties);

     final DynamicMethod constructor = new DynamicMethod("new");
     final Method[] constructors = { constructor };
     helloWorldType.setConstructors(constructors);

     {
     final Code code = constructor.getMethodCode();
     final Statement statement1 = new Statement();
     final Statement statement2 = new Statement();
     final Statement[] statements = { statement1, statement2 };
     code.setStatements(statements);

     final PropertyInvocation propertyInvocation1 = new PropertyInvocation(stringProperty);
     final Literal literal1 = new Literal(MutableString.TYPE, "Hello World!");
     final Executable[] executables1 = { literal1, propertyInvocation1 };
     statement1.setExecutables(executables1);

     final PropertyInvocation propertyInvocation2 = new PropertyInvocation(intProperty);
     final Literal literal2 = new Literal(MutableInteger.TYPE, 8);
     final Executable[] executables2 = { literal2, propertyInvocation2 };

     statement2.setExecutables(executables2);
     }
     final DynamicMethod messageMethod = new DynamicMethod("message", MutableString.TYPE);
     final Method[] methods = { messageMethod };
     helloWorldType.setMethods(methods);

     {
     final Code code = messageMethod.getMethodCode();
     constructCode(helloWorldType, code);
     }

     final MethodInvocation methodInvocation = new MethodInvocation(messageMethod);
     final ExecutionContext ctx2 = new ExecutionContext(vm);
     final Instance<DynamicType> helloWorld = helloWorldType.newInstance(ctx2);
     ctx2.mContextObject = helloWorld;
     final Object returnValue2 = methodInvocation.execute(ctx2);
     }

     private void constructCode(final DynamicType pType, final Code pCode) {
     final Statement statement = new Statement();
     final Statement[] statements = { statement };
     final Property stringProperty = pType.getProperty("string");
     final Property intProperty = pType.getProperty("x");

     final Return returns = new Return();

     final PropertyInvocation propertyInvocation = new PropertyInvocation(intProperty);

     // 4 + 5 - 2 * x -> (- (+ 4 5) (* 2 x))

     // 4 > 5 > + > 2 > x > * -

     final Addition a = new Addition();
     final Multiplication m = new Multiplication();
     final Subtraction s = new Subtraction();

     s.setFirstOperand(a);
     s.setSecondOperand(m);

     a.setFirstOperand(Literal.create(4));
     a.setSecondOperand(Literal.create(5));

     m.setFirstOperand(Literal.create(2));
     m.setSecondOperand(propertyInvocation);

     final Executable[] executables = { s, returns };
     statement.setExecutables(executables);
     pCode.setStatements(statements);
     }*/
}

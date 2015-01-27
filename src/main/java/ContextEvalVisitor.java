
import java.util.HashMap;
import java.util.Map;

public class ContextEvalVisitor extends EvalVisitor {
	Map<String, Double> context = new HashMap<String, Double>();

	public ContextEvalVisitor(Map<String, Double> context) {
		this.context = context;
	}

	/** ID */
	@Override
	public Double visitIdentifier(LEMSExpressionParser.IdentifierContext ctx) {
		String id = ctx.ID().getText();
		//TODO: check for undefined ids
		return context.get(id); 
	}

}

import java.util.ArrayList;
import java.util.List;

public class ListVariablesInExprVisitor extends LEMSExpressionBaseVisitor<String> {
	List<String> variablesInExpr = new ArrayList<String>();

	public List<String> getVariablesInExpr() {
		return variablesInExpr;
	}

	/** expr */
	@Override
	public String visitBase(LEMSExpressionParser.BaseContext ctx) {
		visit(ctx.expr());
		System.out.println("variables in expr:" + variablesInExpr);
		return variablesInExpr.toString(); 
	}

	/** ID */
	@Override
	public String visitId(LEMSExpressionParser.IdContext ctx) {
		String id = ctx.ID().getText();
		variablesInExpr.add(id);
		return id; 
	}

}

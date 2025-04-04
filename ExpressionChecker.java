package classAssignments;

import objectStack.ObjectStack;
import java.util.*;
import java.util.regex.Pattern;

public class ExpressionChecker {
	
	public static void main(String[] args) {
		String exp = "(((1+9)+7)*((6-4)/8))";
		
		if(isBalanced(exp)) {
			System.out.printf("%s is balanced\n", exp);
		}
		else {
			System.out.printf("%s is NOT balanced\n", exp);
		}
		
		if(isBalanced(exp)) {
			double solution = evaluate(exp);
			System.out.println("Solution: " + solution);
		}
	}
	
	public static boolean isBalanced(String expression) {
		final char LEFT_NORMAL = '(';
		final char RIGHT_NORMAL = ')';
		final char LEFT_CURLY = '{';
		final char RIGHT_CURLY = '}';
		final char LEFT_SQUARE = '[';
		final char RIGHT_SQUARE = ']';
		
		ObjectStack stack = new ObjectStack();
		int i;
		boolean failed = false;
		
		for(i = 0; !failed && (i < expression.length()); i++) {
			switch (expression.charAt(i)) {
				case LEFT_NORMAL:
				case LEFT_CURLY:
				case LEFT_SQUARE:
					stack.push(expression.charAt(i));
					break;
				case RIGHT_NORMAL:
					if(stack.isEmpty() || ((char) stack.pop() != LEFT_NORMAL)) {
						failed = true;
					}
					break;
				case RIGHT_CURLY:
					if(stack.isEmpty() || ((char) stack.pop() != LEFT_CURLY)) {
						failed = true;
					}
					break;
				case RIGHT_SQUARE:
					if(stack.isEmpty() || ((char) stack.pop() != LEFT_SQUARE)) {
						failed = true;
					}
					break;
			}
		}
		return (stack.isEmpty() && !failed);
	}
	
	public static double evaluate(String expression) {
		// Two generic stacks to hold the expression’s numbers and operations:
		Stack<Double> numbers = new Stack<Double>( );
		Stack<Character> operations = new Stack<Character>( );
		
		final Pattern CHARACTER = Pattern.compile("\\S.*?");
		final Pattern UNSIGNED_DOUBLE = Pattern.compile("((\\d+\\.?\\d*)|(\\.\\d+))([Ee][-+]?\\d+)?.*?");
		
		// Convert the expression to a Scanner for easier processing. The next String holds the
		// next piece of the expression: a number, operation, or parenthesis.
		Scanner input = new Scanner(expression);
		String next;
		
		while (input.hasNext( )){
			if (input.hasNext(UNSIGNED_DOUBLE)){ // The next piece of the expression is a number
				next = input.findInLine(UNSIGNED_DOUBLE);
				numbers.push(new Double(next));
			}
			else{ 
		// The next piece of the input is an operation (+, -, *, or /) or a parenthesis.
				next = input.findInLine(CHARACTER);
				switch (next.charAt(0)){
					case '+': // Addition
					case '-': // Subtraction
					case '*': // Multiplication
					case '/': // Division
						operations.push(next.charAt(0));
						break;
					case ')': // Right parenthesis (the evaluateStackTops function is on the next page)
						evaluateStackTops(numbers, operations);
						break;
					case '(': // Left parenthesis
						break;
					default : // Illegal character
						throw new IllegalArgumentException("Illegal character");
				}
			}
		}
		if (numbers.size( ) != 1) {
			throw new IllegalArgumentException("Illegal input expression");
		}
		return numbers.pop( );
	}
	
	public static void evaluateStackTops(Stack<Double> numbers, Stack<Character> operations) {
		
		double operand1, operand2;
		
		// Check that the stacks have enough items, and get the two operands.
		if ((numbers.size( ) < 2) || (operations.isEmpty( ))) {
			throw new IllegalArgumentException("Illegal expression");
		}
		
		operand2 = numbers.pop( );
		operand1 = numbers.pop( );
		
		// Carry out an action based on the operation on the top of the stack.
		switch (operations.pop( )){
		case '+': numbers.push(operand1 + operand2);
				  break;
		case '-': numbers.push(operand1 - operand2);
				  break;
		case '*': numbers.push(operand1 * operand2);
                  break;
		case '/': // Note: A division by zero is possible. The result would be one of the
		          // constants Double.POSITIVE_INFINITY or Double.NEGATIVE_INFINITY.
		          numbers.push(operand1 / operand2);
		          break;
		default : throw new IllegalArgumentException("Illegal operation");
		}
	}
}

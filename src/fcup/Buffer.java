package fcup;

import java.util.LinkedList;

public class Buffer {

	private LinkedList<StringBuilder> lineList = new LinkedList<StringBuilder>();
	private Cursor currentCursor = new Cursor (0,0);
	private int beginMarkRow, beginMarkCol; // inicio de marcação para clipboard
	private int endMarkRow, endMarkCol; // fim de marcação para clipboard
	private boolean marked = false;
	private StringBuilder clipboard;

	public void setBeginMark(int line, int col){
		beginMarkRow = line;
		beginMarkCol = col;
		marked = true;
	}

	public void setEndMark(int line, int col){
		endMarkRow = line;
		endMarkCol = col;
	}

	public void unsetMarks(){
		marked = false;
	}

	public boolean getMarked(){
		return marked;
	}

	public void copy(){


	}

	public void paste(){


	}


	// Construir um buffer vazio
	public Buffer() {
		StringBuilder vazia = new StringBuilder();
		lineList.add(vazia);
	}

	// Contruir um buffer já com uma linha
	public Buffer(String linhatemp) {
		StringBuilder linha = new StringBuilder(linhatemp);
		lineList.add(linha);
		StringBuilder vazia = new StringBuilder();
		lineList.add(vazia);
	}

	public int getNumLines(){
		return lineList.size();
	}

	public StringBuilder getNthLine(int i){
		return lineList.get(i);
	}

	public LinkedList<StringBuilder> getAllLines(){
		return lineList;
	}

	//--------------------------------------------------------------//
	//-------------funções para obter e mover o cursor--------------//
	//--------------------------------------------------------------//

	public boolean validPosition(Cursor newCursor){
		int l = newCursor.getL();
		int c = newCursor.getC();

		return( l>=0 && l<lineList.size() && c >= 0 && c<= lineList.get(l).length() );
	}

	public Cursor getCursor(){
		return currentCursor;
	}

	public void setCursor(Cursor newCursor){
		int l = newCursor.getL();
		int c = newCursor.getC();

		if ( validPosition(newCursor)){
			currentCursor.setL(l);
			currentCursor.setC(c);
		}
		else{
			System.out.println("Buffer.setCursor - Invalid Position -" + " l: " + l + " c: " + c);
		}
	}

	//Mover o cursor para o caracter anterior(onde quer que o caracter esteja)
	public void movePrev() {
		int l = currentCursor.getL();
		int c = currentCursor.getC();

		if ( c > 0){
			currentCursor.setC(c-1);
		}
		else if( l > 0){
			currentCursor = new Cursor(l-1, lineList.get(l-1).length());
		}
		else{
			System.out.println("Already at the begining");
		}

	}

	//Mover o cursor para o caracter seguinte(onde quer que o caracter esteja)
	public void moveNext(){
		int l = currentCursor.getL();
		int c = currentCursor.getC();

		int len = lineList.get(l).length();

		if ( c < len ){
			currentCursor.setC(c+1);
		}
		else if( l+1 < getNumLines()){
			currentCursor = new Cursor(l+1, 0);
		}
		else{
			//System.out.println("Already at the end");
		}

	}

	public void movePrevLine(){
		int l = currentCursor.getL();
		int c = currentCursor.getC();

		if(l>0){
			currentCursor = new Cursor(l-1, Math.min(c, lineList.get(l-1).length() ));
		}
		else{
			System.out.println("Already at the first line of the buffer");
		}
	}

	public void moveEndLine(){
		int l = currentCursor.getL();
		int c = currentCursor.getC();
		StringBuilder sb = lineList.get(l);
		int tamanho = sb.length();

		if(c<tamanho-1){
			currentCursor = new Cursor(l,tamanho-1);
		}
		else{
			System.out.println("Already at the end of the line");
		}
	}

	public void moveStartLine(){
		int l = currentCursor.getL();
		int c = currentCursor.getC();

		if(c>0){
			currentCursor = new Cursor(l,0);
		}
		else{
			System.out.println("Already at the begining of the line");
		}
	}

	public void moveNextLine(){
		int l = currentCursor.getL();
		int c = currentCursor.getC();

		if(l<lineList.size()-1){
			currentCursor = new Cursor(l+1, Math.min(c, lineList.get(l+1).length() ));
		}
		else{
			System.out.println("Already at the last line of the buffer");
		}
	}


	//--------------------------------------------------------------//
	//------------funções para inserir/apagar caracteres------------//
	//--------------------------------------------------------------//

	public void insertStr(String linha){

		if (linha.contains("\n")) { throw new Error("Buffer.insertStr : newline in text"); }

		int l = currentCursor.getL();
		int c = currentCursor.getC();

		StringBuilder sb = lineList.get(l);
		sb.insert(c, linha);
		currentCursor = new Cursor(l, c+linha.length() );
	}

	public void insertLn(){
		int l = currentCursor.getL();
		int c = currentCursor.getC();

		StringBuilder sb = lineList.get(l);
		String st1 = sb.substring(0, c);
		String st2 = sb.substring(c, sb.length() );
		StringBuilder sb1 = new StringBuilder(st1);
		StringBuilder sb2 = new StringBuilder(st2);
		lineList.set(l, sb1);
		lineList.add(l+1, sb2);
		currentCursor = new Cursor(l+1,0);
	}

	public void insertChar(char c){
		if(c=='\n'){
			insertLn();
		}
		else{
			insertStr(Character.toString(c));
		}
	}

	public void deleteChar(){
		int l = currentCursor.getL();
		int c = currentCursor.getC();

		if(c>0){
			StringBuilder sb = lineList.get(l);
			sb.deleteCharAt(c-1); // apagar caracter à frente do cursor
			currentCursor = new Cursor(l, c-1);
		}
		else if(l>0){
			deleteLn();
		}
		else{
			System.out.println("Already at the beginning of the buffer");
		}

	}

	private void deleteLn() {
		int l = currentCursor.getL();

		StringBuilder sb1 = lineList.get(l);   // guardar o que está na linha corrente
		StringBuilder sb2 = lineList.get(l-1); // guardar o que está na linha anterior
		int prevLineLength = sb2.length();     // guardar o comprimemto da linha anterior para mover para lá o cursor no fim do delete
		StringBuilder sb3 = sb2.append(sb1);   // juntar as strings

		lineList.remove(l);                    // remover a linha currente da lista
		lineList.set(l-1,sb3);                 // colocar string nova(junção das duas) na lista

		currentCursor = new Cursor(l-1, prevLineLength); //actualizar o cursor para a nova posição(linha acima)
	}

}
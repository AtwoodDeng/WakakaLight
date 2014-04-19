char val;
int ledpin = 4;
const int init_frequency = 9600;


int redPin = 8;
int greenPin = 9;
int bluePin = 10;

void setup()
{
  Serial.begin(init_frequency);
  pinMode(ledpin , OUTPUT);
  
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);  
}

void loop()
{
  val = Serial.read();
  if( val == 'q' )
  {
    
    setColor( 240 , 30 , 30 );
    digitalWrite(ledpin,HIGH);
    Serial.println("Recieve q!");
    int r = readChar();
    Serial.print("Red: ");
    Serial.println(r);
    int g = readChar();
    Serial.print("Green: ");
    Serial.println(g);
    int b = readChar();
    Serial.print("Blue: ");
    Serial.println(b);
    setColor( (int)r , (int)g , (int)b );
    
  }else if ( val == 'w' )
  {
    digitalWrite(ledpin,LOW);
    Serial.println("Recieve w!");
  }else
  {
    //Serial.print("Recieve ");
    //Serial.println(val);
  }
  delay(500);
}

char readChar()
{
  while( Serial.available() <= 0 )
  {
  }
  return Serial.read();
}

void setColor(int red, int green, int blue)
{
  analogWrite(redPin, (red));
  analogWrite(greenPin, (green));
  analogWrite(bluePin, (blue));  
}

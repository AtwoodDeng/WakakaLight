char val;
int ledpin = 4;
const int init_frequency = 9600;

void setup()
{
  Serial.begin(init_frequency);
  pinMode(ledpin , OUTPUT);
}

void loop()
{
  val = Serial.read();
  if( val == 'q' )
  {
    digitalWrite(ledpin,HIGH);
    //Serial.println("Recieve q!");
  }else if ( val == 'w' )
  {
    digitalWrite(ledpin,LOW);
    //Serial.println("Recieve w!");
  }else
  {
    //Serial.print("Recieve ");
    //Serial.println(val);
  }
  delay(500);
}

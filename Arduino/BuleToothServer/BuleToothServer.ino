char val;
int buttonOnPin = 2;
int buttonOffPin = 6;
int ledOnPin =13;
int ledOffPin = 10;
const int init_frequency = 9600;

void setup()
{
  Serial.begin(init_frequency);
  pinMode(buttonOnPin , INPUT);
  pinMode(buttonOffPin , INPUT);
  pinMode(ledOnPin , OUTPUT);
  pinMode(ledOffPin , OUTPUT);
}

void loop()
{
//  if( digitalRead(buttonOnPin)==HIGH )
//  {
//    Serial.print('q');
//    Serial.println("On");
//    digitalWrite(ledOnPin,HIGH);
//    digitalWrite(ledOffPin,LOW);
//  }else if ( digitalRead(buttonOffPin)==HIGH )
//  {
//    Serial.print('w');
//    Serial.println("Off");
//    digitalWrite(ledOnPin,LOW);
//    digitalWrite(ledOffPin,HIGH);
//  }else
//  {
//  }
  if ( digitalRead(buttonOnPin) == HIGH )
  {
    Serial.print('q');
    digitalWrite(ledOnPin,HIGH);
  }else
  {
    Serial.print('w');
    digitalWrite(ledOnPin,LOW);
  }
  delay(50);
}

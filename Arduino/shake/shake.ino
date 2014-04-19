const int HPin = 2;
const int lightPin = 4;

int HState = 0;

void setup() {
  pinMode(HPin, INPUT );
  pinMode(lightPin ,OUTPUT);
}

void loop(){
  HState = digitalRead(HPin);
  if (HState == HIGH ) {
    digitalWrite(lightPin , HIGH );
  }else{
    digitalWrite(lightPin , LOW );
    delay(2000);
  }
}

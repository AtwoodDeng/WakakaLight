int redPin = 9;
int greenPin = 10;
int bluePin = 11;

int isPouringPin = 2;
int colorGreenPin = 3;
int colorBluePin = 4;

const int init_frequency = 9600;

byte id = 0;
byte colorID; //1, green; 2, blue
byte isPouring;
byte data[4];

void setup()
{
  Serial.begin(init_frequency);
  setColor(0, 0, 0);
  data[0] = id;
  data[3] = '#';
  
  pinMode(isPouringPin , INPUT);
  pinMode(colorGreenPin , INPUT);
  pinMode(colorBluePin , INPUT);
  
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
}

void loop(){
  if(Serial.available()) {
    readData();
    printData();
  }
  delay(100);
}

void readData() {
  if(Serial.available() >= 4) {
    int r, g, b, temp;
    r = Serial.read();
    g = Serial.read();
    b = Serial.read();
    temp = Serial.read();
    if(temp == '#') {
      setColor(r, g, b);
    }
  }
}

void printData() {
  if(digitalRead(colorGreenPin) == LOW) {
    colorID = colorGreenPin - 2;
  } else if(digitalRead(colorBluePin) == LOW) {
    colorID = colorBluePin - 2;
  } else {
    colorID = 0;
  }
  
  if(digitalRead(isPouringPin) == HIGH) {
    isPouring = 1;
  } else {
    isPouring = 0;
  }
  
  data[1] = colorID;
  data[2] = isPouring;
  
  Serial.write(data, 4);
}

void setColor(int red, int green, int blue)
{
  analogWrite(redPin, 255-red);
  analogWrite(greenPin, 255-green);
  analogWrite(bluePin, 255-blue);  
}

int redPin = 9;
int greenPin = 10;
int bluePin = 11;

int isPouredPin = 2;
int isPouringPin = 3;

const int init_frequency = 9600;

byte id = 3;
byte data[6];
byte isPoured, isPouring, isShaking, isBumping;
byte preIsPouring, shakingTimer, shakingTimes, startCounting;

int timer;
int stopShaking;

void setup()
{
  Serial.begin(init_frequency);
  setColor(0, 0, 0);
  //setColor(255, 255, 255);
  data[0] = id;
  data[5] = '#';
  startCounting = 0;
  timer = 0;
  stopShaking = 0;
  
  pinMode(isPouredPin , INPUT);
  pinMode(isPouringPin , INPUT);
  
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
}

void loop(){
  if(stopShaking) {
    if(timer < 100) {
      timer++;
    } else {
      stopShaking = 0;
    }
  } else {
    testShaking();
  }
  if(Serial.available()) {
    readData();
    printData();
  }
  
  /*
  if(digitalRead(isPouredPin) == HIGH) {
    setColor(25, 0, 0);
  } else {
    setColor(0, 0, 0);;
  }
  
  if(digitalRead(isPouringPin) == HIGH) {
    setColor(0, 25, 0);
  } else {
    setColor(0, 0, 0);;
  }*/
  
  delay(10);
}

void readData() {
  if(Serial.available() >= 4) {
    byte r, g, b, temp;
    r = Serial.read();
    g = Serial.read();
    b = Serial.read();
    temp = Serial.read();
    setColor(r, g, b);
    if(temp == '*') {
       isShaking = 0;
       timer = 0;
       stopShaking = 1;
    }
  }
}

void testShaking() {
  if(digitalRead(isPouringPin) == HIGH) {
    isPouring = 1;
  } else {
    isPouring = 0;
  }
  
  if(startCounting == 0 && isPouring == 1) {
    preIsPouring = 1;
    shakingTimer = 0;
    shakingTimes = 0;
    startCounting = 1;
  }
  if(startCounting) {
    if(shakingTimer == 100) {
      startCounting = 0;
      if(shakingTimes > 2) {
        isShaking = 1;
      }
    } else {
      if(isPouring != preIsPouring) {
        shakingTimes++;
        preIsPouring = isPouring;
      }
      shakingTimer++;
    }
  }
}

void printData() {
  if(digitalRead(isPouredPin) == HIGH) {
    isPoured = 1;
  } else {
    isPoured = 0;
  }
  
  isBumping = 0;
  
  data[1] = isPoured;
  data[2] = isPouring;
  data[3] = isShaking;
  data[4] = isBumping;
  
  Serial.write(data, 6);
}

void setColor(int red, int green, int blue)
{
  analogWrite(redPin, 255-red);
  analogWrite(greenPin, 255-green);
  analogWrite(bluePin, 255-blue);  
}

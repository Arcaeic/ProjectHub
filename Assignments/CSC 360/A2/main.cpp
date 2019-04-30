#include <iostream>
#include <vector>
#include <cstring>
#include <algorithm>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <unistd.h>

using namespace std;

struct Train {
    int trainID{};
    string direction;
    int priority{};
    int loadingTime{};
    int crossingTime{};

    void printInfo() {
        cout << "Train ID: " << trainID << " "
             << "Direction: " << direction << " "
             << "Priority: " << priority << " "
             << "Loading Time: " << loadingTime << " "
             << "Crossing Time: " << crossingTime << endl;
    }

    bool operator < (const Train& otherTrain) const {
        return (loadingTime < otherTrain.loadingTime);
    }

};

void trainThread(Train train);
Train currTrain;
int trainsCrossed = 0;
int trainsReady = 0;
int totalTrains = 0;
bool trainCrossing = false;
mutex mu, stationmu, crossmu;
condition_variable globalStart, cross;
struct timespec start, stop;
auto *trainStationEastHigh = new vector<Train>;
auto *trainStationEast = new vector<Train>;
auto *trainStationWestHigh = new vector<Train>;
auto *trainStationWest = new vector<Train>;

void printStationInfo() {
    cout << "West High" << endl;
    for (auto &train : *trainStationWestHigh) {
        train.printInfo();
    }

    cout << "East High" << endl;
    for (auto &train : *trainStationEastHigh) {
        train.printInfo();
    }

    cout << "West" << endl;
    for (auto &train : *trainStationWest) {
        train.printInfo();
    }

    cout << "East" << endl;
    for (auto &train : *trainStationEast) {
        train.printInfo();
    }
}

bool goingEast(char* direction) {
    return (strcmp(direction, "e") == 0 || strcmp(direction, "E") == 0);
}

void pushTrainIntoThread(char *currDir, Train train) {
    if (strcmp(currDir, "W") == 0 || strcmp(currDir, "E") == 0) {
        train.priority = 1;
    } else {
        train.priority = 0;
    }

    if (goingEast(currDir)) {
        train.direction = "East";
        thread newThread(trainThread, train);
        newThread.detach();
    } else {
        train.direction = "West";
        thread newThread(trainThread, train);
        newThread.detach();

    }
}

double stopTime() {
    timespec_get(&stop, TIME_UTC);
    return ( stop.tv_sec - start.tv_sec )
           + ( stop.tv_nsec - start.tv_nsec )
             / 1000000000.0;
}

char* getTime() { ;
    double currTime = stopTime();
    int minutes = 0;
    int hours = 0;
    if (currTime >= 60.0) {
        minutes = (int) currTime / 60;
        currTime = fmod(currTime, 60);
    }

    if (minutes >= 60) {
        hours = minutes / 60;
        minutes = minutes % 60;
    }
    auto *buf = (char *) malloc(sizeof(char) * 180);
    sprintf(buf, "%02d:%02d:%04.1f ", hours, minutes, currTime);
    return buf;
}

void loadIntoStation(Train train) {
    if (train.direction == "East") {
        if (train.priority == 1) {
            trainStationEastHigh->insert(trainStationEastHigh->end(), train);
        } else {
            trainStationEast->insert(trainStationEast->end(), train);
        }
    } else {
        if (train.priority == 1) {
            trainStationWestHigh->insert(trainStationWestHigh->end(), train);
        } else {
            trainStationWest->insert(trainStationWest->end(), train);
        }
    }

}

void deleteTrain(Train train) {

    for(unsigned long i = 0; i < trainStationWestHigh->size(); i++) {
        if(trainStationWestHigh->at(i).trainID == train.trainID) {
            trainStationWestHigh->erase(trainStationWestHigh->begin()+i);
            return;
        }
    }

    for(unsigned long i = 0; i < trainStationWest->size(); i++) {
        if(trainStationWest->at(i).trainID == train.trainID) {
            trainStationWest->erase(trainStationWest->begin()+i);
            return;

        }
    }

    for(unsigned long i = 0; i < trainStationEastHigh->size(); i++) {
        if(trainStationEastHigh->at(i).trainID == train.trainID) {
            trainStationEastHigh->erase(trainStationEastHigh->begin()+i);
            return;
        }
    }

    for(unsigned long i = 0; i < trainStationEast->size(); i++) {
        if(trainStationEast->at(i).trainID == train.trainID) {
            trainStationEast->erase(trainStationEast->begin()+i);
            return;
        }
    }
}

void trainThread(Train train) {
    //Wait to load
    unique_lock<mutex> startLock(mu);
    globalStart.wait(startLock);
    startLock.unlock();

    //Load
    usleep((__useconds_t)100000 * train.loadingTime);

    //Change train status
    unique_lock<mutex> stationAdd(stationmu);
    loadIntoStation(train);
    printf("%sTrain %2d is ready to go %4s\n", getTime(), train.trainID, train.direction.data());
    trainsReady++;
    stationAdd.unlock();

    //Wait for signal to cross
    unique_lock<mutex> crossLock(crossmu);
    cross.wait(crossLock, [train](){return train.trainID == currTrain.trainID;});

    //Cross and signal
    printf("%sTrain %2d is ON the main track going %4s\n", getTime(), train.trainID, train.direction.data());
    trainCrossing = true;
    stationmu.lock();
    deleteTrain(train);
    stationmu.unlock();
    trainsCrossed++;
    usleep((__useconds_t)100000 * train.crossingTime);
    printf("%sTrain %2d is OFF the main track after going %4s\n", getTime(), train.trainID, train.direction.data());


    trainsReady--;
    crossLock.unlock();
    trainCrossing = false;

}

void startTime() {
    timespec_get(&start, TIME_UTC);
}

void parseInputFile(FILE *file) {

    Train train;
    int i = 0;
    char currDir[2];
    do {
        fscanf(file, "%s", currDir);
        fscanf(file, "%i", &train.loadingTime);
        fscanf(file, "%i", &train.crossingTime);
        train.trainID = i++;
        pushTrainIntoThread(currDir, train);

    } while (!feof(file));
    totalTrains = i;
}

void findNextTrain() {
//    printStationInfo();
    //High priority - Opposing Directions
    if (!trainStationWestHigh->empty() && !trainStationEastHigh->empty()) {
        if (currTrain.direction == "West" || trainsCrossed == 0) {
            Train eastHighPick = trainStationEastHigh->front();
            for (auto &train : *trainStationEastHigh) {
                if (train.loadingTime < eastHighPick.loadingTime
                    || (train.loadingTime == eastHighPick.loadingTime && train.trainID < eastHighPick.trainID)) {
                    eastHighPick = train;
                }
            }
            currTrain = eastHighPick;
            return;
        } else {
            Train westHighPick = trainStationWestHigh->front();
            for (auto &train : *trainStationWestHigh) {
                if (train.loadingTime < westHighPick.loadingTime
                    || (train.loadingTime == westHighPick.loadingTime && train.trainID < westHighPick.trainID)) {
                    westHighPick = train;
                }
            }
            currTrain = westHighPick;
            return;
        }

        //High Priority - Same Directions
    } else if (!trainStationWestHigh->empty() && trainStationEastHigh->empty()) {
        Train westHighPick = trainStationWestHigh->front();
        for (auto &train : *trainStationWestHigh) {
            if (train.loadingTime == westHighPick.loadingTime && train.trainID < westHighPick.trainID) {
                westHighPick = train;
            }
        }
        currTrain = westHighPick;
        return;

    } else if (!trainStationEastHigh->empty() && trainStationWestHigh->empty()) {
        Train eastHighPick = trainStationEastHigh->front();
        for (auto &train : *trainStationEastHigh) {
            if (train.loadingTime == eastHighPick.loadingTime && train.trainID < eastHighPick.trainID) {
                eastHighPick = train;
            }
        }
        currTrain = eastHighPick;
        return;
    }

    //Low priority - Opposing Directions
    if (!trainStationWest->empty() && !trainStationEast->empty()) {
        if (currTrain.direction == "West" || trainsCrossed == 0) {
            Train eastPick = trainStationEast->front();
            for (auto &train : *trainStationEast) {
                if (train.loadingTime < eastPick.loadingTime
                    || (train.loadingTime == eastPick.loadingTime && train.trainID < eastPick.trainID)) {
                    eastPick = train;
                }
            }
            currTrain = eastPick;
            return;
        } else {
            Train westPick = trainStationWest->front();
            for (auto &train : *trainStationWest) {
                if (train.loadingTime < westPick.loadingTime
                    || (train.loadingTime == westPick.loadingTime && train.trainID < westPick.trainID)) {
                    westPick = train;
                }
            }
            currTrain = westPick;
            return;
        }

        //Low Priority - Same Directions
    } else if (!trainStationWest->empty() && trainStationEast->empty()) {
        Train westPick = trainStationWest->front();
        for (auto &train : *trainStationWest) {
            if (train.loadingTime == westPick.loadingTime && train.trainID <= westPick.trainID) {
                westPick = train;
            }
        }
        currTrain = westPick;
        return;
    } else if (!trainStationEast->empty() && trainStationWest->empty()) {
        Train eastPick = trainStationEast->front();
        for (auto &train : *trainStationEast) {
            if (train.loadingTime == eastPick.loadingTime && train.trainID <= eastPick.trainID) {
                eastPick = train;
            }
        }
        currTrain = eastPick;;
        return;
    }
}

int main(int argc, char* argv[]) {
    //Read input file
    FILE *file = fopen(argv[1], "r+");
    parseInputFile(file);

    //Broadcast
    usleep(1000000);
    startTime();
    globalStart.notify_all();

    //Check/wait for ready trains
    while (trainsCrossed != totalTrains) {
        if (trainsReady > 0 && !trainCrossing) {
            stationmu.lock();
            findNextTrain();
            stationmu.unlock();
        }
        cross.notify_one();
        while (trainCrossing) {}
    }
}

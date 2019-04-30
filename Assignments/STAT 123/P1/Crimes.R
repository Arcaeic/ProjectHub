###############################################################################
##########               FINAL PROJECT STARTER CODE                 ##########
###############################################################################
# clear workspace
rm(list=ls())

# import required libraries
library(tidyverse)
library(readxl)
library(lubridate)

# helper function to fix time formatting
fix.time = function(x) {
  return(str_split(x, " ", simplify=T)[1,2])
}


#' clean.my.data
#'
#' @param data the full 919 report data 
#'
#' @return cleaned version of the full data (some observations removed for too many NA's)
#' @export
#'
#' @examples df = clean.my.data(data.all)
clean.my.data = function(data) {
  df = data %>%
    select(-contains("Attended")) %>%
    select(-contains("Incident")) %>%
    select(-c(`Gender`, Height, `Weight (lbs)`, `Hair Colour`, `Call initiated to: Police (911)`, 
              `Call initiated to: Fire (911)`,`Vehicle ID`, `Other: (Enter)`)) %>%
    mutate(`Call initiated to: Ambulance  (911)` = as.numeric(`Call initiated to: Ambulance  (911)`)) %>%
    mutate(`Call initiated to: Ambulance  (911)` = if_else(is.na(`Call initiated to: Ambulance  (911)`), 
                                                           0, `Call initiated to: Ambulance  (911)`)) %>%
    mutate(`Emergency Services Required` = as.numeric(`Emergency Services Required`)) %>%
    mutate(`Emergency Services Required` = if_else(is.na(`Emergency Services Required`), 
                                                   0, `Emergency Services Required`)) %>%
    mutate(`Event Location: Drop In` = as.numeric(`Event Location: Drop In`)) %>%
    mutate(`Event Location: Drop In` = if_else(is.na(`Event Location: Drop In`), 
                                               0, `Event Location: Drop In`)) %>%
    mutate(`Event Location: Lobby` = as.numeric(`Event Location: Lobby`)) %>%
    mutate(`Event Location: Lobby` = if_else(is.na(`Event Location: Lobby`), 
                                             0, `Event Location: Lobby`)) %>%
    mutate(`Event Location: Courtyard` = as.numeric(`Event Location: Courtyard`)) %>%
    mutate(`Event Location: Courtyard` = if_else(is.na(`Event Location: Courtyard`), 
                                                 0, `Event Location: Courtyard`)) %>%
    mutate(`Event Location: Kitchen` = as.numeric(`Event Location: Kitchen`)) %>%
    mutate(`Event Location: Kitchen` = if_else(is.na(`Event Location: Kitchen`), 
                                               0, `Event Location: Kitchen`)) %>%
    mutate(`Event Location: Dinning Area` = as.numeric(`Event Location: Dinning Area`)) %>%
    mutate(`Event Location: Dinning Area` = if_else(is.na(`Event Location: Dinning Area`), 
                                                    0, `Event Location: Dinning Area`)) %>%
    mutate(`Event Location: Chapel` = as.numeric(`Event Location: Chapel`)) %>%
    mutate(`Event Location: Chapel` = if_else(is.na(`Event Location: Chapel`), 
                                              0, `Event Location: Chapel`)) %>%
    mutate(`Event Location: Hygiene` = as.numeric(`Event Location: Hygiene`)) %>%
    mutate(`Event Location: Hygiene` = if_else(is.na(`Event Location: Hygiene`), 
                                               0, `Event Location: Hygiene`)) %>%
    mutate(`Event Location: Games room` = as.numeric(`Event Location: Games room`)) %>%
    mutate(`Event Location: Games room` = if_else(is.na(`Event Location: Games room`), 
                                                  0, `Event Location: Games room`)) %>%
    mutate(`Event Location: Shipping&Reveiving Area` = as.numeric(`Event Location: Shipping&Reveiving Area`)) %>%
    mutate(`Event Location: Shipping&Reveiving Area` = if_else(is.na(`Event Location: Shipping&Reveiving Area`), 
                                                               0, `Event Location: Shipping&Reveiving Area`)) %>%
    mutate(`Event Location: Common Housing Area` = as.numeric(`Event Location: Common Housing Area`)) %>%
    mutate(`Event Location: Common Housing Area` = if_else(is.na(`Event Location: Common Housing Area`), 
                                                           0, `Event Location: Common Housing Area`)) %>%
    mutate(`Event Location: In Room` = as.numeric(`Event Location: In Room`)) %>%
    mutate(`Event Location: In Room` = if_else(is.na(`Event Location: In Room`), 
                                               0, `Event Location: In Room`)) %>%
    mutate(`Event Location: Perimeter` = as.numeric(`Event Location: Perimeter`)) %>%
    mutate(`Event Location: Perimeter` = if_else(is.na(`Event Location: Perimeter`), 
                                                 0, `Event Location: Perimeter`)) %>%
    mutate(`Event Location:Snack Bar` = as.numeric(`Event Location:Snack Bar`)) %>%
    mutate(`Event Location:Snack Bar` = if_else(is.na(`Event Location:Snack Bar`), 
                                                0, `Event Location:Snack Bar`)) %>%
    mutate(`Naloxone` = as.numeric(`Naloxone`)) %>%
    mutate(`Naloxone` = if_else(is.na(`Naloxone`), 
                                0, `Naloxone`)) %>%
    mutate(`Overdose` = as.numeric(`Overdose`)) %>%
    mutate(`Overdose` = if_else(is.na(`Overdose`), 
                                0, `Overdose`)) %>%
    drop_na()
  
  df$Time = sapply(df$Time, FUN=fix.time)
  
  df = df %>%
    mutate(Time=hms::as.hms(Time))
}

###############################################################################
##########                      VERY IMPORTANT                       ##########
###############################################################################
# This must be an "absolute" file path. This means you can't just place the 
# 919report_data.xlsx file in the same folder and expect it to run properly. 
# The "read_excel" function requires that you use an absolute file path. Below
# is what the correct file path looks like on my mac laptop. If you are using
# a pc, the syntax is slightly different. You need to replace the line below
# with the ABSOLUTE FILE PATH ON YOUR COMPUTER.

file_path = "/home/arcaeic/Desktop/STAT123/STATS 123 P1/919report_data.xlsx"

# if I was using a windows based computer, my file path might look something like this
# file_path = "C:/Users/stevehof/OneDrive/Documents/tutoring/Stats_123/919report_data.xlsx"

# this line loads the original data into R
data.all = read_excel(file_path, col_types=c("numeric", "text", "date", rep("text",39)))

# this line will "clean" data.all into a usable format and store it in a variable
# called df
df = clean.my.data(data.all)


###############################################################################
##########       Put the code for your assignment below here         ##########
###############################################################################
require(ggplot2)

# (1). Summary Statistics (for pre-cleaned data)
#We can see that time and Case # are the only numeric data available. The remainder is char.
summary(data.all)

# (Fun) Amount of Case #
length(df$`Case #`)
length(data.all$`Case #`)
# (Fun) Proportion of Involved
A1_Prop <- (c(df$Involved[df$Involved == "A1"]))
A1_Prop <- A1_Prop[!is.na(A1_Prop)]
A2_Prop <- (c(df$Involved[df$Involved == "A2"]))
A2_Prop <- A2_Prop[!is.na(A2_Prop)]
A3_Prop <- (c(df$Involved[df$Involved == "A3"]))
A3_Prop <- A3_Prop[!is.na(A3_Prop)]
A4_Prop <- (c(df$Involved[df$Involved == "A4"]))
A4_Prop <- A4_Prop[!is.na(A4_Prop)]
A5_Prop <- (c(df$Involved[df$Involved == "A5"]))
A5_Prop <- A5_Prop[!is.na(A5_Prop)]
size = length(df$Involved)
incident_prop <- c(length(A1_Prop)/size, length(A2_Prop)/size, length(A3_Prop)/size, length(A4_Prop)/size, length(A5_Prop)/size)
names(incident_prop) <- c("Family", "Resident", "Building", "Staff", "Other")


# (Fun) Consequently, we can see that the majority of cases are family-related
incident_prop

# (2) From this, we can see the majority of incidents involving Naloxone occuring during the evening
ggplot(aes(x = Time, y = Naloxone), data = df) + geom_point() +  geom_smooth(method = "lm", se = FALSE)

# (2) Drug/Alcohol Abuse occurs at during the mid/late afternoon.
ggplot(aes(x = Time, y = `Incident Teyp(S):  Drug/Alcohol`), data= filter(data.all, data.all$`Incident Teyp(S):  Drug/Alcohol` == 1 & !is.na(data.all$`Incident Teyp(S):  Drug/Alcohol`))) + geom_point()

# (2) Overall, we see the majority of cases happening in the mid/late afternoon. Though the data is bimodal and is skewed left.
ggplot(aes(x = `Time`), data = df) + geom_histogram()

# (3) Number of Overdose calls to ambulances:
length(filter(data.all, data.all$Overdose == 1 & data.all$`Call initiated to: Ambulance  (911)` == 1))

# (4) Number of calls to first-responders (per group):
# We can see the amount of calls to each type of first-responder is identical
responders <- data.frame(
                "Ambulance" = length(filter(data.all, data.all$`Call initiated to: Ambulance  (911)` == 1)),
                "Police" = length(filter(data.all, data.all$`Call initiated to: Police (911)` == 1)),
                "Fire" = length(filter(data.all, data.all$`Call initiated to: Fire (911)` == 1))
              )
responders

# (5) Correlation between Time and Place (Omitted locations with no incidents)

# Drop In seems to have a large variance, and therefore a low correlation to time
ggplot(aes(x = Time, y = `Event Location: Drop In`), data = filter(df, df$`Event Location: Drop In` == 1)) + geom_point()
# Lobby incidences are positively correlated with time (i.e. more incidences happen at night)
ggplot(aes(x = Time, y = `Event Location: Lobby`), data = filter(df, df$`Event Location: Lobby` == 1)) + geom_point()
# Courtyard incidences are positively correlated with time
ggplot(aes(x = Time, y = `Event Location: Courtyard`), data = filter(df, df$`Event Location: Courtyard` == 1)) + geom_point()
# "Dinning Area" has weak correlation, incidences are few and happen during early morning or night
ggplot(aes(x = Time, y = `Event Location: Dinning Area`), data = filter(df, df$`Event Location: Dinning Area` == 1)) + geom_point()
# "Hygiene" incidences positively correlate with time
ggplot(aes(x = Time, y = `Event Location: Hygiene`), data = filter(df, df$`Event Location: Hygiene` == 1)) + geom_point()
# Games room is a single point and therefore has 0 correlation
ggplot(aes(x = Time, y = `Event Location: Games room`), data = filter(df, df$`Event Location: Games room` == 1)) + geom_point()
# Common Housing Area is a single point and therefore has 0 correlation
ggplot(aes(x = Time, y = `Event Location: Common Housing Area`), data = filter(df, df$`Event Location: Common Housing Area` == 1)) + geom_point()
# In Room will have an extremely weak correlation, as there are two observations and occur at opposite ends of the day
ggplot(aes(x = Time, y = `Event Location: In Room`), data = filter(df, df$`Event Location: In Room` == 1)) + geom_point()
# Perimeter is positively correlated, as more incidences happen later in the day
ggplot(aes(x = Time, y = `Event Location: Perimeter`), data = filter(df, df$`Event Location: Perimeter` == 1)) + geom_point()


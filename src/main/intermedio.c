#include<stdio.h>
#include<string.h>
#include<stdlib.h>
typedef int bool;
#define false 0
#define true 1
typedef char* string;
char *concatUtils;
char *convertUtils;
char *itsUtils(int var) {convertUtils = malloc(512);sprintf(convertUtils,"%d",var);return convertUtils;}
char *dtsUtils(double var) {convertUtils = malloc(512);sprintf(convertUtils,"%lf",var);return convertUtils;}
char *btsUtils(bool var) {convertUtils = malloc(512);if(var)sprintf(convertUtils,"%s","true");else sprintf(convertUtils,"%s","false");return convertUtils;}
char *ctsUtils(char var) {convertUtils = malloc(512);sprintf(convertUtils,"%c",var);return convertUtils;}
char *ccUtils(char *var1, char *var2) {concatUtils = malloc(512);sprintf(concatUtils,"%s%s",var1,var2);return concatUtils;}
int i=0,f=1,app;
bool lettura=false;
void fattoriale(int  i,int  *f){
while((i > 0)){
*f = *f * i;
i = i - 1;
}
printf("%s",ccUtils(itsUtils(*f),"\n" ));

}
int main(){
while(!(lettura)){
printf("%s","Inserisci un numero intero positivo :");
scanf("%d",&i);
app = i;
if((app > 0)){
lettura = true;
}
else{
lettura = false;
}
}
fattoriale(i,&f);
printf("%s%s%s%s","Il fattoriale di ",itsUtils(i)," e' ",itsUtils(f));
free(concatUtils);
free(convertUtils);
return 0;
}
% Pedir al usuario la frecuencia de corte
fc = input('Digita la frecuencia de corte: ');

% Frecuencia de corte
omegac = fc*pi; 

% Pedir la usuario la longitud del filtro
L = input('Digital la longitud del filtro: ');		     

% Desplazamiento de muestras
M = (L-1)/2;     

% Indexa los coeficientes
l = 0:2*M;       

% Calculo de los coeficientes
h = omegac/pi*sinc(omegac*(l-M)/pi);  

% Calculo del rango de frecuencia
omega = -pi:2*pi/200:pi;

% Calculo de la frecuencia de respuesta
Hd = freqz(h,1,omega); 

% Creacion y graficacion de la señal
plot((omega/pi),abs(Hd)),... 
    xlabel('Frecuencia normalizada'), ylabel('Magnitud'), grid;
axis([-1 1 0 1.2]);
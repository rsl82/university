OUTPUT = client
CFLAGS = -g -Wall -Wvla -I inc -D_REENTRANT
LFLAGS = -L lib -lSDL2 -lSDL2_image -lSDL2_ttf -lpthread

%.o: %.c %.h
	gcc $(CFLAGS) -c -o $@ $<

%.o: %.c
	gcc $(CFLAGS) -c -o $@ $<

all: $(OUTPUT)

runclient: $(OUTPUT)
	LD_LIBRARY_PATH=lib ./client localhost 2000

client: client.o csapp.o
	gcc $(CFLAGS) -o $@ $^ $(LFLAGS)

server: server.o csapp.o
	gcc $(CFLAGS) -o $@ $^ $(LFLAGS)

clean:
	rm -f $(OUTPUT) *.o

import random
import csv


def function():
    letters = [chr(i) for i in range(ord('A'), ord('Z') + 1)]  
    grade = ['A', 'B', 'C', 'D', 'F']

    file_path = 'C:/Users/frost/Downloads/random_letters.csv'
    id1 = ['625583103', '192000823', '992801501', '114468620', '323784372', '509323562', '332731339', '782329646', '293783982', '481017836', '341130905', '940046009', '593838010', '269738101', '995572019', '787959897', '118099453', '800855459', '365181558', '109622581', '426473223', '479436823', '987376987', '245217736', '911060078']
    with open(file_path, 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(["ID", "Letter", "Grade"]) 
        for item in id1:
            random_letters = random.sample(letters, 5) 
            for letter in random_letters:
                random_grade = random.choice(grade)
                writer.writerow([item, letter, random_grade])

    id2 = ['560110069', '788662787', '974251032', '802002026', '360464878', '971811685', '187081349', '530308465', '679428228', '895228427', '193503270', '641537666', '872804769', '845635343', '281101394', '747263606', '240240439', '392409594', '133810112', '713834072', '124755714', '807452839', '981231511', '659454216', '742513829']
    with open(file_path, 'a', newline='') as file:
        writer = csv.writer(file)
        for item in id2:
            random_letters = random.sample(letters, 15) 
            for letter in random_letters:
                random_grade = random.choice(grade)
                writer.writerow([item, letter, random_grade])

    id3 = ['844757448', '989109357', '219408069', '706785219', '840374589', '106889234', '761598123', '492999362', '194109266', '134366288', '547182944', '384970356', '512682104', '133026094', '187024837', '179944026', '625677166', '458970578', '486327063', '173961806', '165935824', '815646106', '549663507', '676985691', '107337921']
    with open(file_path, 'a', newline='') as file:
        writer = csv.writer(file)
        for item in id3:
            random_letters = random.sample(letters, 22) 
            for letter in random_letters:
                random_grade = random.choice(grade)
                writer.writerow([item, letter, random_grade])

    id4 = ['144850747', '810198380', '661864218', '855571301', '343535079', '665170100', '388832602', '249136647', '829965057', '314360350', '619794430', '994049087', '686959422', '203318708', '956941997', '557557449', '272564129', '710096333', '925438079', '857793074', '847752462', '636270882', '991990840', '377169378', '154901196']
    with open(file_path, 'a', newline='') as file:
        writer = csv.writer(file)
        for item in id4:
            random_letters = random.sample(letters, 25) 
            for letter in random_letters:
                random_grade = random.choice(grade)
                writer.writerow([item, letter, random_grade])


if __name__ == '__main__':
    function()
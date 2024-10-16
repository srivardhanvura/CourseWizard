import random

# last_names = []
# first_names = []

# with open("D:/Downloads/names/last-names.txt", 'r') as f:
#     for _ in range(1500):
#         name = f.readline().strip()
#         last_names.append(name)
        
# with open("D:/Downloads/names/first-names.txt", 'r') as f:
#     for _ in range(1500):
#         name = f.readline().strip()
#         first_names.append(name)
        

# command1 = 'insert into users values("{user_name}","{noop}pass",1);' 
# # command2 = 'insert into roles values("{user_name}", "ROLE_STUDENT");'
# command2 = 'insert into roles values("{user_name}", "ROLE_INSTRUCTOR");'
# # command3 = 'insert into student(first_name, last_name, user_name, email, department) values("{first_name}", "{last_name}", "{user_name}", "{email}@gmail.om", "{dept}");'
# command3= 'insert into instructor(first_name, last_name, user_name, email, office, department) values("{first_name}", "{last_name}", "{user_name}", "{email}@gmail.om", "EB-1 {room}", "{dept}");'

# final_commands = []

# depts = ["CSC", "IT", "CIV", "EEE", "ECE"]

# for i, dept in enumerate(depts):
#     st = 1000+ (i*3)
#     end = 1000+ ((i+1)*3)
    
#     # st = (i*10)
#     # end = ((i+1)*10)
    
#     for j in range(st, end):
#         values = {
#             "email": first_names[j],
#             "user_name": f"{first_names[j]}_{last_names[j]}",
#             "first_name": first_names[j].capitalize(),
#             "last_name": last_names[j].capitalize(),
#             "dept": dept,
#             "noop": "{noop}",
#             "room": random.randint(1000, 9000)
#         }
#         final_commands.append(command1.format(**values))
#         final_commands.append(command2.format(**values))
#         final_commands.append(command3.format(**values))
    
# # with open("D:/Desktop/auto_insert.sql", 'w') as f:
# #     for item in final_commands:
# #         f.write(item + "\n")
        
# with open("D:/Desktop/auto_insert2.sql", 'w') as f:
#     for item in final_commands:
#         f.write(item + "\n")

# command = 'insert into review(comment, rating, course_id) values("{comment}", {rating}, {course_id});'
# final_commands = []
# comments = ["Good", "Bad", "Average", "Can be better", "Best Course", "Liked it!", "Will suggest to a friend", "Loved it!!"]

# for i in range(1, 35):
#     for j in range(3):
#         values = {
#                 "comment": comments[random.randint(0, len(comments)-1)],
#                 "rating": random.randint(0, 5),
#                 "course_id": i
#             }
#         final_commands.append(command.format(**values))


# with open("D:/Desktop/auto_insert3.sql", 'w') as f:
#     for item in final_commands:
#         f.write(item + "\n")

command = 'insert into course_student values({course_id}, {student_id});'
courses = {
    "CSC": [1,2,3,4,5,6,7,8],
    "IT": [9,10,11,12,13,14],
    "EEE": [15,16,17,18,19,20],
    "ECE": [21,22,23,24,25,26],
    "CIV": [27,28,29,30,31,32]
}

students = {
    "CSC":[i for i in range(8, 19)],
    "IT":[i for i in range(19, 29)],
    "CIV":[i for i in range(29, 39)],
    "EEE":[i for i in range(39, 49)],
    "ECE":[i for i in range(49, 59)],
}
all_commands = []

students["CSC"].append(1)
students["IT"].append(2)
students["IT"].append(7)
students["CIV"].append(3)
students["EEE"].append(5)
students["ECE"].append(4)
students["ECE"].append(6)

for course in courses:
    for student in students[course]:
        random_sample = random.sample(courses[course], 3)
        for samp in random_sample:
            values = {
                "course_id": samp,
                "student_id": student
            }
            all_commands.append(command.format(**values))

with open("D:/Desktop/auto_insert4.sql", 'w') as f:
    for item in all_commands:
        f.write(item + "\n")
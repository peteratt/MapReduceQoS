import sys

if len(sys.argv) < 2:
  print("Usage: " + sys.argv[0] + " <logfile>")
  sys.exit(1)

try:
  log_file = open(sys.argv[1], 'r')
except:
  print("Error opening: " + sys.argv[1] + ". Exiting.")
  sys.exit(2)

post_jobs = []
nonconf_jobs = []

qos = set()

for line in log_file:
  tokens = line.split()
  # Malformed line if less than 4 tokens
  if len(tokens) == 3:
    if (tokens[0] == "posted"):
      post_jobs.append({'id' : int(tokens[1]), 'qos' : int(tokens[2])})
      qos.add(int(tokens[2]))
    elif (tokens[0] == "nonConf"):
      nonconf_jobs.append({'id' : int(tokens[2]), 'qos' : int(tokens[2])})

# start_jobs and end_jobs filled

qos_results = []
qos_sizes = []

for q in qos:
  post_q = [job for job in post_jobs if job['qos'] == q]
  nonc_q = [job for job in nonconf_jobs if job['qos'] == q]
  qos_sizes.append({'qos' : q, 'posted' : len(post_q),
                               'nonconf' : len(nonc_q)})

#  not_ended = 0.0
#  count = 0
#  acum = 0.0
#
#  for started_job in started_q:
#    ended_job = [job for job in end_q if job['id'] == started_job['id']]
#    if len(ended_job) == 0:
#      # Job didn't end
#      not_ended += 1
#    else:
#      ended_job = ended_job[0]
#      acum += ended_job['time'] - started_job['time']
#      count += 1
#


for result in qos_sizes:
  print(str(result['qos']) + ',\t' + str(result['posted']) + ',\t'
      + str(result['nonconf']) + ',\t' + str(result['nonconf']/result['posted']))

#print("Total jobs started: " + str(len(start_jobs)))
#print("Total jobs ended: " + str(len(end_jobs)))
#for x in qos_sizes:
#  print("Qos: " + str(x['qos']) + "\tEnded/Started: " + str(x['ended']) + "/" +
#      str(x['started']))

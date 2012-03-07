import sys

if len(sys.argv) < 2:
  print("Usage: " + sys.argv[0] + " <logfile>")
  sys.exit(1)

try:
  log_file = open(sys.argv[1], 'r')
except:
  print("Error opening: " + sys.argv[1] + ". Exiting.")
  sys.exit(2)

start_jobs = []
end_jobs = []

qos = set()

for line in log_file:
  tokens = line.split()
  # Malformed line if less than 4 tokens
  if len(tokens) == 4:
    if (tokens[0] == "start"):
      start_jobs.append({'time' : int(tokens[1]), 'id' : int(tokens[2]), 'qos' :
        int(tokens[3])})
      qos.add(int(tokens[3]))
    elif (tokens[0] == "end"):
      end_jobs.append({'time' : int(tokens[1]), 'id' : int(tokens[2]), 'qos' :
        int(tokens[3])})

# start_jobs and end_jobs filled

qos_results = []
qos_sizes = []

for q in qos:
  started_q = [job for job in start_jobs if job['qos'] == q]
  end_q = [job for job in end_jobs if job['qos'] == q]
  qos_sizes.append({'qos' : q, 'started' : len(started_q),
                               'ended' : len(end_q)})

  

  not_ended = 0.0
  count = 0
  acum = 0.0

  for started_job in started_q:
    ended_job = [job for job in end_q if job['id'] == started_job['id']]
    if len(ended_job) == 0:
      # Job didn't end
      not_ended += 1
    else:
      ended_job = ended_job[0]
      acum += ended_job['time'] - started_job['time']
      count += 1

  if (count > 0):
    qos_results.append({'mean': acum/count, 'failed': not_ended, 'qos': q})
  else:
    qos_results.append({'mean': -1 , 'failed': -1, 'qos': q})


for result in qos_results:
  print(str(result['qos']) + ',\t' + str(result['mean']) + ',\t'
      + str(result['failed']))

#print("Total jobs started: " + str(len(start_jobs)))
#print("Total jobs ended: " + str(len(end_jobs)))
#for x in qos_sizes:
#  print("Qos: " + str(x['qos']) + "\tEnded/Started: " + str(x['ended']) + "/" +
#      str(x['started']))

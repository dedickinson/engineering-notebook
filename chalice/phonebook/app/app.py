from chalice import Chalice

app = Chalice(app_name='phonebook')

GROUPS = {'0':'Finance', '1': 'IT'}

@app.route('/', methods=['GET'])
def index():
    return {'hello': 'world'}

@app.route('/groups', methods=['GET'])
def groups():
    return {'result': GROUPS}

@app.route('/groups/{group_id}', methods=['GET'])
def groups(group_id):
    return {
        'request': {'group_id': group_id},
        'result': GROUPS[group_id]}


